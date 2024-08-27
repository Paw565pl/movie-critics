package dev.paw565pl.movieCritics.movie.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

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
  private List<Movie> movies;
}
