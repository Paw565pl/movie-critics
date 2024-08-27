package dev.paw565pl.movieCritics.movie.repository;

import dev.paw565pl.movieCritics.movie.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepository extends JpaRepository<Writer, Long> {}
