package dev.paw565pl.movie_critics.rating.model;

import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.user.model.UserEntity;
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
        name = "ratings",
        indexes = {
                @Index(
                        name = "one_rating_for_movie_per_user",
                        columnList = "user_id, movie_id",
                        unique = true)
        })
public class RatingEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "value", nullable = false)
    private Byte value;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private UserEntity author;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MovieEntity movie;
}
