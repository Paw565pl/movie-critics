package dev.paw565pl.movie_critics.comment.model;

import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.user.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "comments",
        indexes = {
            @Index(
                    name = "one_comment_for_movie_per_user",
                    columnList = "user_id, movie_id",
                    unique = true)
        })
public class Comment {

    @Id @GeneratedValue private Long id;

    @NonNull @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @NonNull @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @NonNull @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    private Movie movie;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
