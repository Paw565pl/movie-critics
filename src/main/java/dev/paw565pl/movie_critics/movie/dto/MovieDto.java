package dev.paw565pl.movie_critics.movie.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record MovieDto(
        @NotBlank(message = "Title cannot be empty.")
                @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters.")
                String title,
        @Size(min = 2, max = 255, message = "Age rating must be between 2 and 255 characters.") String ageRating,
        @NotNull(message = "Released date cannot be null.") LocalDate released,
        @Size(min = 2, max = 255, message = "Runtime must be between 2 and 255 characters.") String runtime,
        @Size(min = 10, max = 1000, message = "Plot must be between 10 and 1000 characters.") String plot,
        @Size(min = 2, max = 255, message = "Language must be between 2 and 255 characters.") String language,
        @Size(min = 2, max = 255, message = "Country must be between 2 and 255 characters.") String country,
        @Size(min = 2, max = 255, message = "Awards must be between 2 and 255 characters.") String awards,
        @Size(min = 2, max = 255, message = "Poster must be between 2 and 255 characters.") String poster,
        @Max(value = 100, message = "Metascore must be less than or equal to 100.")
                @Min(value = 0, message = "Metascore must be greater than or equal to 0.")
                Short metaScore,
        @Size(min = 2, max = 255, message = "Dvd must be between 2 and 255 characters.") String dvd,
        @Size(min = 2, max = 255, message = "Box office must be between 2 and 255 characters.") String boxOffice,
        @Size(min = 2, max = 255, message = "Website must be between 2 and 255 characters.") String website,
        @NotEmpty(message = "Genre ids cannot be empty.") List<Long> genreIds,
        List<Long> directorIds,
        List<Long> writerIds,
        List<Long> actorIds) {}
