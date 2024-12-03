package dev.paw565pl.movie_critics.user.response;

import dev.paw565pl.movie_critics.user.model.OAuthProvider;
import java.util.UUID;
import lombok.Data;

@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private OAuthProvider provider;
    private Long ratingsCount;
    private Long commentsCount;
}
