package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.Movie;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository
        extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.movie.id = :movieId")
    Long countRatingsByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT ROUND(AVG(r.value), 2) FROM Rating r WHERE r.movie.id = :movieId")
    Optional<Double> findAverageRatingByMovieId(@Param("movieId") Long movieId);

    Page<Movie> findAllByUsersWhoWantToWatchId(UUID userId, Pageable pageable);

    Optional<Movie> findByIdAndUsersWhoWantToWatchId(Long id, UUID userId);
}
