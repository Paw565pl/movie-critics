package dev.paw565pl.movie_critics.movie_to_watch.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie_to_watch.dto.MovieToWatchDto;
import dev.paw565pl.movie_critics.movie_to_watch.service.MovieToWatchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies-to-watch")
public class MovieToWatchRestController {

    private final MovieToWatchService movieToWatchService;

    public MovieToWatchRestController(MovieToWatchService movieToWatchService) {
        this.movieToWatchService = movieToWatchService;
    }

    @IsAuthenticated
    @GetMapping
    public Page<MovieResponse> findAll(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        return movieToWatchService.findAll(UserDetailsImpl.fromJwt(jwt), pageable);
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody MovieToWatchDto movieToWatchDto) {
        return movieToWatchService.create(UserDetailsImpl.fromJwt(jwt), movieToWatchDto);
    }

    @IsAuthenticated
    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        movieToWatchService.delete(movieId, UserDetailsImpl.fromJwt(jwt));
    }
}
