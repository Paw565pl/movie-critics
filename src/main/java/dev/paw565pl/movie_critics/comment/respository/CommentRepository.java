package dev.paw565pl.movie_critics.comment.respository;

import dev.paw565pl.movie_critics.comment.model.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByMovieId(Long movieId, Pageable pageable);

    Optional<Comment> findByIdAndMovieId(Long id, Long movieId);
}
