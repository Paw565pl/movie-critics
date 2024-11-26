package dev.paw565pl.movie_critics.rating.repository;

import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    Optional<RatingEntity> findByMovieIdAndAuthorId(Long movieId, UUID authorId);
}
