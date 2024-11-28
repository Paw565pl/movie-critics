package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MovieRepository
        extends JpaRepository<MovieEntity, Long>, JpaSpecificationExecutor<MovieEntity> {
    Page<MovieEntity> findAllByUsersWhoWantToWatchId(UUID userId, Pageable pageable);

    Optional<MovieEntity> findByIdAndUsersWhoWantToWatchId(Long id, UUID userId);

    Page<MovieEntity> findAllByUsersWhoFavoritedId(UUID userId, Pageable pageable);

    Optional<MovieEntity> findByIdAndUsersWhoFavoritedId(Long id, UUID userId);

    Page<MovieEntity> findAllByUsersWhoIgnoredId(UUID userId, Pageable pageable);

    Optional<MovieEntity> findByIdAndUsersWhoIgnoredId(Long id, UUID userId);

    @Query("SELECT DISTINCT m.ageRating FROM MovieEntity m WHERE m.ageRating IS NOT NULL")
    Set<String> findDistinctAgeRatings();
}
