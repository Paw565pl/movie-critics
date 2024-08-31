package dev.paw565pl.movie_critics.movie.controller;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Page<MovieResponse> findAll(MovieFilterDto filters, Pageable pageable) {
        return movieService.findAll(filters, pageable);
    }

    @GetMapping("/{id}")
    public MovieResponse findById(@PathVariable Long id) {
        return movieService.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@Valid @RequestBody MovieDto dto) {
        return movieService.create(dto);
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieDto dto) {
        return movieService.update(id, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }
}
