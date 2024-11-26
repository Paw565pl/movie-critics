package dev.paw565pl.movie_critics.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public record MovieDto(
        @NotBlank(message = "Title cannot be empty.")
        @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters.")
        String title,
        Year year,
        String ageRating,
        LocalDate released,
        String runtime,
        @NotEmpty(message = "Genre ids cannot be empty.") List<Long> genreIds,
        List<Long> directorIds,
        List<Long> writerIds,
        List<Long> actorIds,
        @Size(min = 10, max = 1000, message = "Plot must be between 10 and 1000 characters.")
        String plot,
        String language,
        String country,
        String awards,
        String poster,
        Short metaScore,
        String dvd,
        String boxOffice,
        String website) {
}
