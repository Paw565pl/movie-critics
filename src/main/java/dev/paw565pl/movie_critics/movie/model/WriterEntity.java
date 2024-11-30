package dev.paw565pl.movie_critics.movie.model;

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
        name = "writers",
        indexes = {
            @Index(name = "index_writer_name", columnList = "name", unique = true),
        })
public class WriterEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "writers")
    private List<MovieEntity> movies = new ArrayList<>();
}
