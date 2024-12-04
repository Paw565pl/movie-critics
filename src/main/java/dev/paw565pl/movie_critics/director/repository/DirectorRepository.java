package dev.paw565pl.movie_critics.director.repository;

import dev.paw565pl.movie_critics.director.model.DirectorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<DirectorEntity, Long> {
    Optional<DirectorEntity> findByNameIgnoreCase(String name);
}
