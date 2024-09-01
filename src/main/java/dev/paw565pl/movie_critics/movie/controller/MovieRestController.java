package dev.paw565pl.movie_critics.movie.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import dev.paw565pl.movie_critics.rating.service.RatingService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieRestController {

    private final MovieService movieService;
    private final RatingService ratingService;

    public MovieRestController(MovieService movieService, RatingService ratingService) {
        this.movieService = movieService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public Page<MovieResponse> findAll(MovieFilterDto filters, Pageable pageable) {
        return movieService.findAll(filters, pageable);
    }

    @GetMapping("/{id}")
    public MovieResponse findById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @IsAdmin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@Valid @RequestBody MovieDto dto) {
        return movieService.create(dto);
    }

    @IsAdmin
    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieDto dto) {
        return movieService.update(id, dto);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

    @IsAuthenticated
    @GetMapping("/{id}/rate")
    public RatingResponse findRating(@PathVariable Long id, Authentication auth) {
        return ratingService.findByMovieIdAndUserId(id, UUID.fromString(auth.getName()));
    }

    @IsAuthenticated
    @PostMapping("/{id}/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createRating(
            @PathVariable Long id, @Valid @RequestBody RatingDto dto, Authentication auth) {
        return ratingService.create(id, UUID.fromString(auth.getName()), dto);
    }

    @IsAuthenticated
    @PutMapping("/{id}/rate")
    public RatingResponse updateRating(
            @PathVariable Long id, @Valid @RequestBody RatingDto dto, Authentication auth) {
        return ratingService.update(id, UUID.fromString(auth.getName()), dto);
    }

    @IsAuthenticated
    @DeleteMapping("/{id}/rate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable Long id, Authentication auth) {
        ratingService.delete(id, UUID.fromString(auth.getName()));
    }
}
