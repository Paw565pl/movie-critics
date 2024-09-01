package dev.paw565pl.movie_critics.rating.model;

import dev.paw565pl.movie_critics.movie.model.Movie;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
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

    @NonNull @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NonNull @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}
