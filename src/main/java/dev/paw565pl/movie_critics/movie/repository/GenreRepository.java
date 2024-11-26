package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Optional<GenreEntity> findByNameIgnoreCase(String name);
}
