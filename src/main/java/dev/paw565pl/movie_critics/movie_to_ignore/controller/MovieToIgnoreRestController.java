package dev.paw565pl.movie_critics.movie_to_ignore.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie_to_ignore.dto.MovieToIgnoreDto;
import dev.paw565pl.movie_critics.movie_to_ignore.service.MovieToIgnoreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ignored-movies")
public class MovieToIgnoreRestController {

    private final MovieToIgnoreService movieToIgnoreService;

    public MovieToIgnoreRestController(MovieToIgnoreService movieToIgnoreService) {
        this.movieToIgnoreService = movieToIgnoreService;
    }

    @IsAuthenticated
    @GetMapping
    public Page<MovieResponse> findAll(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        return movieToIgnoreService.findAll(jwt, pageable);
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody MovieToIgnoreDto dto) {
        return movieToIgnoreService.create(jwt, dto);
    }

    @IsAuthenticated
    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        movieToIgnoreService.delete(movieId, jwt);
    }
}
