package dev.paw565pl.movieCritics.movie.repository;

import dev.paw565pl.movieCritics.movie.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {}
