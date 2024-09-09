package dev.paw565pl.movie_critics.favorite_movie.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteMovieDto(@NotNull(message = "MovieId cannot be null.") Long movieId) {}
