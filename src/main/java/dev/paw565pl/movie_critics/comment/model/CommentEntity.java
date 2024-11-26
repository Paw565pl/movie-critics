package dev.paw565pl.movie_critics.comment.model;

import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(
                        name = "one_comment_for_movie_per_user",
                        columnList = "user_id, movie_id",
                        unique = true)
        })
public class CommentEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User author;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MovieEntity movie;
}
