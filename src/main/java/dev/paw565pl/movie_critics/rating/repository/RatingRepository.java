package dev.paw565pl.movie_critics.rating.repository;

import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    Optional<RatingEntity> findByMovieIdAndAuthorId(Long movieId, UUID authorId);
}
