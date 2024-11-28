package dev.paw565pl.movie_critics.movie.model;

import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import dev.paw565pl.movie_critics.user.model.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name = "movies",
        indexes = {
            @Index(name = "index_movie_title", columnList = "title", unique = true),
            @Index(name = "index_movie_released", columnList = "released")
        })
public class MovieEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NonNull @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "year")
    private Year year;

    @Column(name = "age_rating")
    private String ageRating;

    @Column(name = "released")
    private LocalDate released;

    @Column(name = "runtime")
    private String runtime;

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
    private Short metaScore;

    @Column(name = "dvd")
    private String dvd;

    @Column(name = "boxoffice")
    private String boxOffice;

    @Column(name = "website")
    private String website;

    @NonNull @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<GenreEntity> genres;

    @ManyToMany
    @JoinTable(
            name = "movie_directors",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "director_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<DirectorEntity> directors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "movie_writers",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "writer_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<WriterEntity> writers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ActorEntity> actors = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<RatingEntity> ratings = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Formula("(SELECT COUNT(r.id) FROM ratings r WHERE r.movie_id = id)")
    private Long ratingsCount;

    @Setter(AccessLevel.NONE)
    @Formula("(SELECT ROUND(AVG(r.value), 2) FROM ratings r WHERE r.movie_id = id)")
    private Double averageRating;

    @OneToMany(mappedBy = "movie")
    private List<CommentEntity> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "moviesToWatch")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserEntity> usersWhoWantToWatch = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteMovies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserEntity> usersWhoFavorited = new ArrayList<>();

    @ManyToMany(mappedBy = "ignoredMovies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserEntity> usersWhoIgnored = new ArrayList<>();
}
