package dev.paw565pl.movieCritics.movie.service;

import dev.paw565pl.movieCritics.movie.response.MovieResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {

    Page<MovieResponse> findAll(Pageable pageable);

    Optional<MovieResponse> findById(Long id);
}
