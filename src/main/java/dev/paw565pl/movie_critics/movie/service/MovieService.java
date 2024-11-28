package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.specification.MovieSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public MovieEntity findEntity(Long id) {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public Page<MovieResponse> findAll(
            Optional<Jwt> jwt, MovieFilterDto filters, Pageable pageable) {
        var specification =
                Specification.where(MovieSpecification.titleContainsIgnoreCase(filters.title()))
                        .and(MovieSpecification.ageRatingEqualsIgnoreCase(filters.ageRating()))
                        .and(MovieSpecification.releasedAfterOrEquals(filters.startReleasedDate()))
                        .and(MovieSpecification.releasedBeforeOrEquals(filters.endReleasedDate()))
                        .and(MovieSpecification.genreIdsContains(filters.genreIds()))
                        .and(MovieSpecification.directorIdsContains(filters.directorIds()))
                        .and(MovieSpecification.writerIdsContains(filters.writerIds()))
                        .and(MovieSpecification.actorIdsContains(filters.actorIds()))
                        .and(MovieSpecification.languageContainsIgnoreCase(filters.language()))
                        .and(MovieSpecification.countryContainsIgnoreCase(filters.country()));

        if (jwt.isPresent()) {
            var userId = UserDetailsImpl.fromJwt(jwt.get()).getId();
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
    public MovieResponse create(MovieDto dto) {
        var movieEntity = movieMapper.toEntity(dto);

        try {
            var savedMovieEntity = movieRepository.saveAndFlush(movieEntity);
            return movieMapper.toResponse(savedMovieEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie with given title already exists.");
        }
    }

    @Transactional
    public MovieResponse update(Long id, MovieDto dto) {
        var movieEntity = findEntity(id);

        var updatedMovieEntity = movieMapper.toEntity(dto);
        updatedMovieEntity.setId(movieEntity.getId());

        try {
            var savedMovieEntity = movieRepository.saveAndFlush(updatedMovieEntity);
            return movieMapper.toResponse(savedMovieEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie with given title already exists.");
        }
    }

    @Transactional
    public void delete(Long id) {
        var movieEntity = findEntity(id);
        movieRepository.deleteById(movieEntity.getId());
    }
}
