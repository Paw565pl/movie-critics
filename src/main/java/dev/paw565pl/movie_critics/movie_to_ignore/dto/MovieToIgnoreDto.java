package dev.paw565pl.movie_critics.movie_to_ignore.dto;

import jakarta.validation.constraints.NotNull;

public record MovieToIgnoreDto(@NotNull(message = "Movie id cannot be null.") Long movieId) {
}
