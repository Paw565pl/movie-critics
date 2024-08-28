package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.specification.MovieSpecification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public Page<MovieResponse> findAll(
            Pageable pageable,
            String title,
            String rated,
            LocalDate startReleasedDate,
            LocalDate endReleasedDate,
            List<Long> genresIds) {
        var filters =
                Specification.where(MovieSpecification.titleContainsIgnoreCase(title))
                        .and(MovieSpecification.ratedEqualsIgnoreCase(rated))
                        .and(MovieSpecification.releasedAfterOrEquals(startReleasedDate))
                        .and(MovieSpecification.releasedBeforeOrEquals(endReleasedDate))
                        .and(MovieSpecification.genresIdsContains(genresIds));

        return movieRepository.findAll(filters, pageable).map(movieMapper::toResponseDto);
    }

    @Override
    public Optional<MovieResponse> findById(Long id) {
        return movieRepository.findById(id).map(movieMapper::toResponseDto);
    }

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
}
