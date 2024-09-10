package dev.paw565pl.movie_critics.user.repository;

import dev.paw565pl.movie_critics.user.model.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(
            """
                SELECT u FROM User u
                LEFT JOIN Rating r ON r.author = u
                LEFT JOIN Comment c ON c.author = u
                GROUP BY u.id
                HAVING COUNT(r.id) + COUNT(c.id) > 0
                ORDER BY COUNT(r.id) + COUNT(c.id) ASC
            """)
    Page<User> findAllOrderByRatingsAndCommentsCount(Pageable pageable);
}
