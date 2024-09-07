package dev.paw565pl.movie_critics.auth.repository;

import dev.paw565pl.movie_critics.auth.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {}
