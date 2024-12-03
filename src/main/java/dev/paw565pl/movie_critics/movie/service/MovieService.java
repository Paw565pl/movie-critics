package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.image.service.ImageService;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.specification.MovieSpecification;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ImageService imageService;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, ImageService imageService) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.imageService = imageService;
    }

    public MovieEntity findEntity(Long id) {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public Page<MovieResponse> findAll(Optional<UserDetailsImpl> user, MovieFilterDto filters, Pageable pageable) {
        var specification = Specification.where(MovieSpecification.titleContainsIgnoreCase(filters.title()))
                .and(MovieSpecification.ageRatingEqualsIgnoreCase(filters.ageRating()))
                .and(MovieSpecification.releasedAfterOrEquals(filters.startReleasedDate()))
                .and(MovieSpecification.releasedBeforeOrEquals(filters.endReleasedDate()))
                .and(MovieSpecification.genreIdsContains(filters.genreIds()))
                .and(MovieSpecification.directorIdsContains(filters.directorIds()))
                .and(MovieSpecification.writerIdsContains(filters.writerIds()))
                .and(MovieSpecification.actorIdsContains(filters.actorIds()))
                .and(MovieSpecification.languageContainsIgnoreCase(filters.language()))
                .and(MovieSpecification.countryContainsIgnoreCase(filters.country()));

        if (user.isPresent()) {
            var userId = user.get().getId();
            specification = specification.and(MovieSpecification.notIgnoredByUser(userId));
        }

        return movieRepository.findAll(specification, pageable).map(movieMapper::toResponse);
    }

    public MovieResponse findById(Long id) {
        var movieEntity = findEntity(id);
        return movieMapper.toResponse(movieEntity);
    }

    public Set<String> findDistinctAgeRatings() {
        return movieRepository.findDistinctAgeRatings();
    }

    @Transactional
    public MovieResponse create(MovieDto dto, @Nullable MultipartFile poster) {
        var movieEntity = movieMapper.toEntity(dto);
        if (poster != null && !poster.isEmpty()) {
            var posterUrl = imageService.saveFile(poster);
            movieEntity.setPosterUrl(posterUrl);
        }

        try {
            var savedMovieEntity = movieRepository.saveAndFlush(movieEntity);
            return movieMapper.toResponse(savedMovieEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie with given title already exists.");
        }
    }

    @Transactional
    public MovieResponse update(Long id, MovieDto dto, @Nullable MultipartFile poster) {
        var movieEntity = findEntity(id);

        var updatedMovieEntity = movieMapper.toEntity(dto);
        updatedMovieEntity.setId(movieEntity.getId());

        if (poster != null && !poster.isEmpty()) {
            var posterUrl = imageService.saveFile(poster);
            updatedMovieEntity.setPosterUrl(posterUrl);
        } else {
            updatedMovieEntity.setPosterUrl(movieEntity.getPosterUrl());
        }

        try {
            var savedMovieEntity = movieRepository.saveAndFlush(updatedMovieEntity);
            return movieMapper.toResponse(savedMovieEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie with given title already exists.");
        }
    }

    @Transactional
    public MovieResponse updatePoster(Long id, MultipartFile poster) {
        var movieEntity = findEntity(id);

        var posterUrl = imageService.saveFile(poster);
        movieEntity.setPosterUrl(posterUrl);

        var updatedMovieEntity = movieRepository.saveAndFlush(movieEntity);
        return movieMapper.toResponse(updatedMovieEntity);
    }

    @Transactional
    public void deletePoster(Long id) {
        var movieEntity = findEntity(id);
        movieEntity.setPosterUrl(null);
        movieRepository.saveAndFlush(movieEntity);
    }

    @Transactional
    public void delete(Long id) {
        var movieEntity = findEntity(id);
        movieRepository.deleteById(movieEntity.getId());
    }
}
