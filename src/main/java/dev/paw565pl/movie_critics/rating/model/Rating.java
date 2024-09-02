package dev.paw565pl.movie_critics.rating.model;

import dev.paw565pl.movie_critics.movie.model.Movie;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "ratings",
        indexes = {
            @Index(
                    name = "one_rating_for_movie_per_user",
                    columnList = "user_id, movie_id",
                    unique = true)
        })
public class Rating {

    @Id @GeneratedValue private Long id;

    @NonNull @Column(name = "value", nullable = false)
    private Byte value;

    @NonNull @Column(name = "author", nullable = false, updatable = false)
    private String author;

    @NonNull @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @NonNull @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    private Movie movie;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
