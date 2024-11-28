package dev.paw565pl.movie_critics.favorite_movie.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.favorite_movie.dto.FavoriteMovieDto;
import dev.paw565pl.movie_critics.favorite_movie.service.FavoriteMovieService;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite-movies")
public class FavoriteMovieRestController {

    private final FavoriteMovieService favoriteMovieService;

    public FavoriteMovieRestController(FavoriteMovieService favoriteMovieService) {
        this.favoriteMovieService = favoriteMovieService;
    }

    @IsAuthenticated
    @GetMapping
    public Page<MovieResponse> findAll(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        return favoriteMovieService.findAll(jwt, pageable);
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody FavoriteMovieDto dto) {
        return favoriteMovieService.create(jwt, dto);
    }

    @IsAuthenticated
    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        favoriteMovieService.delete(movieId, jwt);
    }
}
