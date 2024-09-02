package dev.paw565pl.movie_critics.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import lombok.NonNull;

public record MovieDto(
        @NonNull @NotBlank(message = "Title cannot be empty.")
                @Size(min = 2, max = 150, message = "Title must be between 2 and 255 characters.")
                String title,
        Year year,
        String rated,
        @NonNull @NotEmpty(message = "GenresIds cannot be empty.") List<Long> genresIds,
        List<Long> directorsIds,
        List<Long> writersIds,
        List<Long> actorsIds,
        LocalDate released,
        String runtime,
        @Size(min = 10, max = 1000, message = "Plot must be between 10 and 1000 characters.")
                String plot,
        String language,
        String country,
        String awards,
        String poster,
        String metaScore,
        String dvd,
        String boxOffice,
        String website) {}
