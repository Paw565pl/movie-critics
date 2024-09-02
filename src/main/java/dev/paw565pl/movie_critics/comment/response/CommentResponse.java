package dev.paw565pl.movie_critics.comment.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdAt;
}
