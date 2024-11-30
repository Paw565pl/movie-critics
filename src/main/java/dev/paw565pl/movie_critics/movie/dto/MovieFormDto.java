package dev.paw565pl.movie_critics.movie.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record MovieFormDto(
        @NotBlank(message = "Title cannot be empty.")
                @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters.")
                String title,
        @Size(max = 255, message = "Age must be at most 255 characters.") String ageRating,
        @NotNull(message = "Released date cannot be empty.") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate released,
        @Size(max = 255, message = "Runtime must be at most 255 characters.") String runtime,
        @Size(min = 10, max = 1000, message = "Plot must be between 10 and 1000 characters.") String plot,
        @Size(max = 255, message = "Language must be at most 255 characters.") String language,
        @Size(max = 255, message = "Country must be at most 255 characters.") String country,
        @Size(max = 255, message = "Awards must be at most 255 characters.") String awards,
        @Size(max = 255, message = "Poster must be at most 255 characters.") String poster,
        @Max(value = 100, message = "Metascore must be less than or equal to 100.")
                @Min(value = 0, message = "Metascore must be greater than or equal to 0.")
                Short metaScore,
        @Size(max = 255, message = "Dvd must be at most 255 characters.") String dvd,
        @Size(max = 255, message = "Box office must be at most 255 characters.") String boxOffice,
        @Size(max = 255, message = "Website must be at most 255 characters.") String website,
        @NotEmpty(message = "Genre ids cannot be empty.") List<Long> genreIds,
        List<Long> directorIds,
        List<Long> writerIds,
        List<Long> actorIds) {}
