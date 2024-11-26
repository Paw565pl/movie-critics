package dev.paw565pl.movie_critics.comment.repository;

import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByMovieId(Long movieId, Pageable pageable);

    Optional<CommentEntity> findByIdAndMovieId(Long id, Long movieId);
}
