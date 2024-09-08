package dev.paw565pl.movie_critics.movie.model;

import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.rating.model.Rating;
import dev.paw565pl.movie_critics.user.model.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
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
            name = "movie_genres",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "movie_directors",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "director_id")})
    private List<Director> directors;

    @ManyToMany
    @JoinTable(
            name = "movie_writers",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "writer_id")})
    private List<Writer> writers;

    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")})
    private List<Actor> actors;

    @OneToMany(mappedBy = "movie")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments;

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

    @ManyToMany(mappedBy = "moviesToWatch")
    private List<User> usersWhoWantToWatch;
}
