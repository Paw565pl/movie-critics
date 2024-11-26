package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<DirectorEntity, Long> {
    Optional<DirectorEntity> findByNameIgnoreCase(String name);
}
