package dev.paw565pl.movie_critics.user.repository;

import dev.paw565pl.movie_critics.user.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query(
            """
                        SELECT u FROM UserEntity u
                        LEFT JOIN Rating r ON r.author = u
                        LEFT JOIN CommentEntity c ON c.author = u
                        GROUP BY u.id
                        HAVING COUNT(r.id) + COUNT(c.id) > 0
                        ORDER BY COUNT(r.id) + COUNT(c.id) DESC
                    """)
    Page<UserEntity> findAllOrderByRatingsAndCommentsCount(Pageable pageable);
}
