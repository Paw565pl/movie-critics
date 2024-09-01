package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {

    Page<MovieResponse> findAll(MovieFilterDto filters, Pageable pageable);

    MovieResponse findById(Long id);

    MovieResponse create(MovieDto dto);

    MovieResponse update(Long id, MovieDto dto);

    void delete(Long id);
}
