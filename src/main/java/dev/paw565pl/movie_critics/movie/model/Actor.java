package dev.paw565pl.movie_critics.movie.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "actors",
        indexes = {
            @Index(name = "index_name", columnList = "name", unique = true),
        })
public class Actor {

    @Id @GeneratedValue private Long id;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "actors")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Movie> movies;
}
