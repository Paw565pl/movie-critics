package dev.paw565pl.movie_critics.top_active_user.response;

import java.util.UUID;
import lombok.Data;

@Data
public class TopActiveUserResponse {
    private UUID id;
    private String username;
    private Long ratingsCount;
    private Long commentsCount;
}
