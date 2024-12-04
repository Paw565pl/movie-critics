package dev.paw565pl.movie_critics.genre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreDto(
        @NotBlank(message = "Name cannot be empty.")
                @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters long.")
                String name) {}
