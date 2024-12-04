package dev.paw565pl.movie_critics.genre.model;

import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name = "genres",
        indexes = {
            @Index(name = "index_genre_name", columnList = "name", unique = true),
        })
public class GenreEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<MovieEntity> movies = new ArrayList<>();
}
