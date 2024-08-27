package dev.paw565pl.movieCritics.movie.repository;

import dev.paw565pl.movieCritics.movie.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {}
