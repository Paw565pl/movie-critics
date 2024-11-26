package dev.paw565pl.movie_critics.movie.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Data
public class MovieResponse {

    private Long id;
    private String title;
    private Year year;
    private String ageRating;
    private LocalDate released;
    private String runtime;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private Short metaScore;
    private String dvd;
    private String boxOffice;
    private String website;
    private List<GenreResponse> genres;
    private List<DirectorResponse> directors;
    private List<WriterResponse> writers;
    private List<ActorResponse> actors;
    private Long ratingsCount;
    private Double averageRating;
}
