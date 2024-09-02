package dev.paw565pl.movie_critics.movie.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "directors",
        indexes = {
            @Index(name = "index_name", columnList = "name", unique = true),
        })
public class Director {

    @Id @GeneratedValue private Long id;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "directors")
    private List<Movie> movies;
}
