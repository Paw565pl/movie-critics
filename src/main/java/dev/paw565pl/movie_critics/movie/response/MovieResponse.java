package dev.paw565pl.movie_critics.movie.response;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieResponse {

    private Long id;
    private String title;
    private Year year;
    private String rated;
    private LocalDate released;
    private String runtime;
    private List<GenreResponse> genres;
    private List<DirectorResponse> directors;
    private List<WriterResponse> writers;
    private List<ActorResponse> actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private String metaScore;
    private String dvd;
    private String boxOffice;
    private String website;
    private Long ratingsCount;
    private Double averageRating;
}
