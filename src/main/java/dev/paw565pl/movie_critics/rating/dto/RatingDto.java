package dev.paw565pl.movie_critics.rating.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record RatingDto(
        @NotNull(message = "Value cannot be null.") @Range(min = 1, max = 5, message = "Value must be between 1 and 5.")
                Byte value) {}
