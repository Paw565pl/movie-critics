package dev.paw565pl.movie_critics.auth.model;

import dev.paw565pl.movie_critics.auth.provider.OAuthProvider;
import jakarta.persistence.*;
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
}
