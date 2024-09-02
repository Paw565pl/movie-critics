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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @GetMapping("/{id}")
    public MovieResponse findById(@PathVariable Long id) {
        return movieService.findById(id);
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
    @GetMapping("/{movieId}/rate")
    public RatingResponse findRating(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        return ratingService.findByMovieIdAndUserId(movieId, jwt);
    }

    @IsAuthenticated
    @PostMapping("/{movieId}/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createRating(
            @PathVariable Long movieId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RatingDto dto) {
        return ratingService.create(movieId, jwt, dto);
    }

    @IsAuthenticated
    @PutMapping("/{movieId}/rate")
    public RatingResponse updateRating(
            @PathVariable Long movieId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RatingDto dto) {
        return ratingService.update(movieId, jwt, dto);
    }

    @IsAuthenticated
    @DeleteMapping("/{movieId}/rate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable Long id, Authentication auth) {
        ratingService.delete(id, UUID.fromString(auth.getName()));
    public void deleteRating(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        ratingService.delete(movieId, jwt);
    }
}
