package dev.paw565pl.movie_critics.movie.repository;

import dev.paw565pl.movie_critics.movie.model.Movie;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovieRepository
        extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Page<Movie> findAllByUsersWhoWantToWatchId(UUID userId, Pageable pageable);

    Optional<Movie> findByIdAndUsersWhoWantToWatchId(Long id, UUID userId);

    Page<Movie> findAllByUsersWhoFavoritedId(UUID userId, Pageable pageable);

    Optional<Movie> findByIdAndUsersWhoFavoritedId(Long id, UUID userId);
}
