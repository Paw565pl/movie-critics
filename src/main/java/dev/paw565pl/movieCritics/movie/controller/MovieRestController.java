package dev.paw565pl.movieCritics.movie.controller;

import dev.paw565pl.movieCritics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movieCritics.movie.response.MovieResponse;
import dev.paw565pl.movieCritics.movie.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Page<MovieResponse> findAll(Pageable pageable) {
        return movieService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public MovieResponse findById(@PathVariable Long id) {
        return movieService.findById(id).orElseThrow(MovieNotFoundException::new);
    }
}
