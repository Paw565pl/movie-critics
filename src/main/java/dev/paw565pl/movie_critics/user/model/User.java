package dev.paw565pl.movie_critics.user.model;

import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.rating.model.Rating;
import dev.paw565pl.movie_critics.user.provider.OAuthProvider;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "users",
        indexes = {
            @Index(
                    name = "unique_user_per_provider",
                    columnList = "username, email, provider",
                    unique = true)
        })
public class User {

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
    private List<Rating> ratings;

    @Formula("(SELECT COUNT(r.id) FROM ratings r WHERE r.user_id = id)")
    private Long ratingsCount;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    @Formula("(SELECT COUNT(c.id) FROM comments c WHERE c.user_id = id)")
    private Long commentsCount;

    @ManyToMany
    @JoinTable(
            name = "user_movies_to_watch",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"),
            indexes =
                    @Index(
                            name = "one_movie_per_user",
                            columnList = "user_id, movie_id",
                            unique = true))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Movie> moviesToWatch;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_movies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"),
            indexes =
                    @Index(
                            name = "one_movie_per_user",
                            columnList = "user_id, movie_id",
                            unique = true))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Movie> favoriteMovies;

    @ManyToMany
    @JoinTable(
            name = "user_ignored_movies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"),
            indexes =
                    @Index(
                            name = "one_movie_per_user",
                            columnList = "user_id, movie_id",
                            unique = true))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Movie> ignoredMovies;
}
