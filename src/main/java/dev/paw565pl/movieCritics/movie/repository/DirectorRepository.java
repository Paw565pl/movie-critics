package dev.paw565pl.movieCritics.movie.repository;

import dev.paw565pl.movieCritics.movie.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {}
