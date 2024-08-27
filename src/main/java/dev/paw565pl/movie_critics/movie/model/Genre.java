package dev.paw565pl.movie_critics.movie.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "genres",
        indexes = {
            @Index(name = "index_name", columnList = "name", unique = true),
        })
public class Genre {

    @Id @GeneratedValue private Long id;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;
}
