package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.WriterEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepository extends JpaRepository<WriterEntity, Long> {
    Optional<WriterEntity> findByNameIgnoreCase(String name);
}
