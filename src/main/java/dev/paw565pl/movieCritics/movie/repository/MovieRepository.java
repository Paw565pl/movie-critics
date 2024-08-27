package dev.paw565pl.movieCritics.movie.repository;

import dev.paw565pl.movieCritics.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
