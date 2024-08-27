package dev.paw565pl.movie_critics.movie.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "movies",
        indexes = {
            @Index(name = "index_title", columnList = "title", unique = true),
            @Index(name = "index_released", columnList = "released")
        })
public class Movie {

    @Id @GeneratedValue private Long id;

    @NonNull @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year")
    private Year year;

    @Column(name = "rating")
    private String rated;

    @Column(name = "released")
    private LocalDate released;

    @Column(name = "runtime")
    private String runtime;

    @NonNull @ManyToMany
    @JoinTable(
            name = "movies_has_genres",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "movies_has_directors",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "director_id")})
    private List<Director> directors;

    @ManyToMany
    @JoinTable(
            name = "movies_has_writers",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "writer_id")})
    private List<Writer> writers;

    @ManyToMany
    @JoinTable(
            name = "movies_has_actors",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")})
    private List<Actor> actors;

    @Column(name = "plot", columnDefinition = "TEXT")
    private String plot;

    @Column(name = "language")
    private String language;

    @Column(name = "country")
    private String country;

    @Column(name = "awards")
    private String awards;

    @Column(name = "poster")
    private String poster;

    @Column(name = "metascore")
    private String metaScore;

    @Column(name = "dvd")
    private String dvd;

    @Column(name = "boxoffice")
    private String boxOffice;

    @Column(name = "website")
    private String website;
}
