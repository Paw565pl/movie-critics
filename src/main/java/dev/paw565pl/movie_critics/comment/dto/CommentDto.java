package dev.paw565pl.movie_critics.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentDto(
        @NotBlank(message = "Text cannot be empty.")
                @Size(min = 3, max = 1000, message = "Text must be between 3 and 1000 characters.")
                String text) {}
