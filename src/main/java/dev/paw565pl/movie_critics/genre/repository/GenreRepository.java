package dev.paw565pl.movie_critics.genre.repository;

import dev.paw565pl.movie_critics.genre.model.GenreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Optional<GenreEntity> findByNameIgnoreCase(String name);
}
