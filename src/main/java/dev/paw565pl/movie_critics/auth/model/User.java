package dev.paw565pl.movie_critics.auth.model;

import dev.paw565pl.movie_critics.auth.provider.OAuthProvider;
import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.rating.model.Rating;
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
}
