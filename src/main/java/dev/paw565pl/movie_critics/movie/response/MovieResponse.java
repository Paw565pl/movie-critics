package dev.paw565pl.movie_critics.movie.response;

import dev.paw565pl.movie_critics.actor.response.ActorResponse;
import dev.paw565pl.movie_critics.director.response.DirectorResponse;
import dev.paw565pl.movie_critics.genre.response.GenreResponse;
import dev.paw565pl.movie_critics.writer.response.WriterResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class MovieResponse {

    private Long id;
    private String title;
    private String ageRating;
    private LocalDate released;
    private String runtime;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String posterUrl;
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
