package dev.paw565pl.movie_critics.movie.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name = "directors",
        indexes = {
            @Index(name = "index_director_name", columnList = "name", unique = true),
        })
public class DirectorEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "directors")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MovieEntity> movies = new ArrayList<>();
}
