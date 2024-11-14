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
        String ageRating,
        @NonNull @NotEmpty(message = "Genre ids cannot be empty.") List<Long> genreIds,
        List<Long> directorIds,
        List<Long> writerIds,
        List<Long> actorIds,
        LocalDate released,
        String runtime,
        @Size(min = 10, max = 1000, message = "Plot must be between 10 and 1000 characters.")
                String plot,
        String language,
        String country,
        String awards,
        String poster,
        Short metaScore,
        String dvd,
        String boxOffice,
        String website) {}
