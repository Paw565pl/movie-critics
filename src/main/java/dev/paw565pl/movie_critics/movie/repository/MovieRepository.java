package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>, JpaSpecificationExecutor<MovieEntity> {
    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.genres WHERE m.id IN :ids")
    List<MovieEntity> findAllWithGenresByIds(@Param("ids") List<Long> ids);

    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.directors WHERE m.id IN :ids")
    List<MovieEntity> findAllWithDirectorsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.writers WHERE m.id IN :ids")
    List<MovieEntity> findAllWithWritersByIds(@Param("ids") List<Long> ids);

    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.actors WHERE m.id IN :ids")
    List<MovieEntity> findAllWithActorsByIds(@Param("ids") List<Long> ids);

    Page<MovieEntity> findAllByUsersWhoWantToWatchId(UUID userId, Pageable pageable);

    Optional<MovieEntity> findByIdAndUsersWhoWantToWatchId(Long id, UUID userId);

    Page<MovieEntity> findAllByUsersWhoFavoritedId(UUID userId, Pageable pageable);

    Optional<MovieEntity> findByIdAndUsersWhoFavoritedId(Long id, UUID userId);

    Page<MovieEntity> findAllByUsersWhoIgnoredId(UUID userId, Pageable pageable);

    Optional<MovieEntity> findByIdAndUsersWhoIgnoredId(Long id, UUID userId);

    @Query("SELECT DISTINCT m.ageRating FROM MovieEntity m WHERE m.ageRating IS NOT NULL")
    Set<String> findDistinctAgeRatings();

    @Query("SELECT DISTINCT m.posterUrl FROM MovieEntity m WHERE m.posterUrl IS NOT NULL")
    Set<String> findDistinctPosterUrls();
}
