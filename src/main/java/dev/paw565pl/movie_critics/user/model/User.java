package dev.paw565pl.movie_critics.user.model;

import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.rating.model.Rating;
import dev.paw565pl.movie_critics.user.provider.OAuthProvider;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @NonNull @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @NonNull @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NonNull @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NonNull @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private OAuthProvider provider;

    @OneToMany(mappedBy = "author")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"),
            name = "user_movies_to_watch",
            indexes =
                    @Index(
                            name = "one_movie_per_user",
                            columnList = "user_id, movie_id",
                            unique = true))
    private List<Movie> moviesToWatch;
}
