package dev.paw565pl.movie_critics.movie.controller;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
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
    public Page<MovieResponse> findAll(
            Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String rated,
            @RequestParam(required = false) LocalDate startReleasedDate,
            @RequestParam(required = false) LocalDate endReleasedDate,
            @RequestParam(required = false) List<Long> genresIds,
            @RequestParam(required = false) List<Long> directorsIds,
            @RequestParam(required = false) List<Long> writersIds,
            @RequestParam(required = false) List<Long> actorsIds,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String country) {
        return movieService.findAll(
                pageable,
                title,
                rated,
                startReleasedDate,
                endReleasedDate,
                genresIds,
                directorsIds,
                writersIds,
                actorsIds,
                language,
                country);
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
}
