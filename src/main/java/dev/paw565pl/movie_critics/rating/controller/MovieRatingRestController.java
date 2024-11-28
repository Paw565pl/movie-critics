package dev.paw565pl.movie_critics.rating.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import dev.paw565pl.movie_critics.rating.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies/{movieId}/rate")
public class MovieRatingRestController {

    private final RatingService ratingService;

    public MovieRatingRestController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @IsAuthenticated
    @GetMapping
    public RatingResponse findRating(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        return ratingService.findByMovieIdAndUserId(movieId, UserDetailsImpl.fromJwt(jwt));
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createRating(
            @PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody RatingDto dto) {
        return ratingService.create(movieId, UserDetailsImpl.fromJwt(jwt), dto);
    }

    @IsAuthenticated
    @PutMapping
    public RatingResponse updateRating(
            @PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody RatingDto dto) {
        return ratingService.update(movieId, UserDetailsImpl.fromJwt(jwt), dto);
    }

    @IsAuthenticated
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt) {
        ratingService.delete(movieId, UserDetailsImpl.fromJwt(jwt));
    }
}
