package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<ActorEntity, Long> {
    Optional<ActorEntity> findByNameIgnoreCase(String name);
}
