package dev.paw565pl.movie_critics.movie_to_watch.dto;

import jakarta.validation.constraints.NotNull;

public record MovieToWatchDto(@NotNull(message = "MovieId cannot be null.") Long movieId) {}
