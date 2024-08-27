package dev.paw565pl.movie_critics.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class MovieDto {

    @NonNull @NotBlank(message = "Title cannot be empty.")
    @Size(min = 2, max = 150, message = "Title must be between 2 and 255 characters.")
    private String title;

    private Year year;
    private String rated;

    @NonNull private List<Long> genresIds;

    private List<Long> directorsIds;
    private List<Long> writersIds;
    private List<Long> actorsIds;
    private LocalDate released;
    private String runtime;

    @Size(min = 10, max = 1000, message = "Plot must be between 10 and 1000 characters.")
    private String plot;

    private String language;
    private String country;
    private String awards;
    private String poster;
    private String metaScore;
    private String dvd;
    private String boxOffice;
    private String website;
}
