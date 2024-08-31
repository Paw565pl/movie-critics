package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.specification.MovieSpecification;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public Page<MovieResponse> findAll(MovieFilterDto filters, Pageable pageable) {
        var specification =
                Specification.where(MovieSpecification.titleContainsIgnoreCase(filters.title()))
                        .and(MovieSpecification.ratedEqualsIgnoreCase(filters.rated()))
                        .and(MovieSpecification.releasedAfterOrEquals(filters.startReleasedDate()))
                        .and(MovieSpecification.releasedBeforeOrEquals(filters.endReleasedDate()))
                        .and(MovieSpecification.genresIdsContains(filters.genresIds()))
                        .and(MovieSpecification.directorsIdsContains(filters.directorsIds()))
                        .and(MovieSpecification.writersIdsContains(filters.writersIds()))
                        .and(MovieSpecification.actorsIdsContains(filters.actorsIds()))
                        .and(MovieSpecification.languageContainsIgnoreCase(filters.language()))
                        .and(MovieSpecification.countryContainsIgnoreCase(filters.country()));

        return movieRepository.findAll(specification, pageable).map(movieMapper::toResponseDto);
    }

    @Override
    public Optional<MovieResponse> findById(Long id) {
        return movieRepository.findById(id).map(movieMapper::toResponseDto);
    }

    @Transactional
    @Override
    public MovieResponse create(MovieDto dto) {
        var movie = movieMapper.toEntity(dto);

        try {
            var savedMovie = movieRepository.save(movie);
            return movieMapper.toResponseDto(savedMovie);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie with given title already exists.");
        }
    }

    @Transactional
    @Override
    public MovieResponse update(Long id, MovieDto dto) {
        movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);

        var updatedMovie = movieMapper.toEntity(dto);
        updatedMovie.setId(id);

        try {
            var savedMovie = movieRepository.save(updatedMovie);
            return movieMapper.toResponseDto(savedMovie);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie with given title already exists.");
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movieRepository.deleteById(id);
    }
}
