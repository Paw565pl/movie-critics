package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = true)
    public Page<MovieResponse> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable).map(movieMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovieResponse> findById(Long id) {
        return movieRepository.findById(id).map(movieMapper::toResponseDto);
    }
}
