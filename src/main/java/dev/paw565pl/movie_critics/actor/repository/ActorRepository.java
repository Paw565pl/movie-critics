package dev.paw565pl.movie_critics.actor.repository;

import dev.paw565pl.movie_critics.actor.model.ActorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<ActorEntity, Long> {
    Optional<ActorEntity> findByNameIgnoreCase(String name);
}
