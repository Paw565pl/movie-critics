package dev.paw565pl.movie_critics.user.model;

import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name = "users",
        indexes = {@Index(name = "unique_user_per_provider", columnList = "username, email, provider", unique = true)})
public class UserEntity {

    @Id
    @NonNull @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @NonNull @Column(name = "username", nullable = false)
    private String username;

    @NonNull @Column(name = "email", nullable = false)
    private String email;

    @NonNull @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private OAuthProvider provider;

    @OneToMany(mappedBy = "author")
    private List<RatingEntity> ratings = new ArrayList<>();

    @Formula("(SELECT COUNT(r.id) FROM ratings r WHERE r.user_id = id)")
    private Long ratingsCount;

    @OneToMany(mappedBy = "author")
    private List<CommentEntity> comments = new ArrayList<>();

    @Formula("(SELECT COUNT(c.id) FROM comments c WHERE c.user_id = id)")
    private Long commentsCount;

    @ManyToMany
    @JoinTable(
            name = "user_movies_to_watch",
            joinColumns =
                    @JoinColumn(
                            name = "user_id",
                            foreignKey =
                                    @ForeignKey(
                                            foreignKeyDefinition =
                                                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE")),
            inverseJoinColumns =
                    @JoinColumn(
                            name = "movie_id",
                            foreignKey =
                                    @ForeignKey(
                                            foreignKeyDefinition =
                                                    "FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE")),
            indexes = @Index(name = "one_movie_to_watch_per_user", columnList = "user_id, movie_id", unique = true))
    private List<MovieEntity> moviesToWatch = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_favorite_movies",
            joinColumns =
                    @JoinColumn(
                            name = "user_id",
                            foreignKey =
                                    @ForeignKey(
                                            foreignKeyDefinition =
                                                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE")),
            inverseJoinColumns =
                    @JoinColumn(
                            name = "movie_id",
                            foreignKey =
                                    @ForeignKey(
                                            foreignKeyDefinition =
                                                    "FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE")),
            indexes = @Index(name = "one_favorite_movie_per_user", columnList = "user_id, movie_id", unique = true))
    private List<MovieEntity> favoriteMovies = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_ignored_movies",
            joinColumns =
                    @JoinColumn(
                            name = "user_id",
                            foreignKey =
                                    @ForeignKey(
                                            foreignKeyDefinition =
                                                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE")),
            inverseJoinColumns =
                    @JoinColumn(
                            name = "movie_id",
                            foreignKey =
                                    @ForeignKey(
                                            foreignKeyDefinition =
                                                    "FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE")),
            indexes = @Index(name = "one_ignored_movie_per_user", columnList = "user_id, movie_id", unique = true))
    private List<MovieEntity> ignoredMovies = new ArrayList<>();
}
