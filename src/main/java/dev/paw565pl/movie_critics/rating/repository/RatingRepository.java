package dev.paw565pl.movie_critics.rating.repository;

import dev.paw565pl.movie_critics.rating.model.Rating;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByMovieIdAndAuthorId(Long movieId, UUID authorId);
}
