package dev.paw565pl.movie_critics.auth.service;

import dev.paw565pl.movie_critics.auth.model.User;
import dev.paw565pl.movie_critics.auth.provider.OAuthProvider;
import dev.paw565pl.movie_critics.auth.repository.UserRepository;
import dev.paw565pl.movie_critics.auth.utils.JwtUtils;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createOrUpdate(Jwt jwt, OAuthProvider provider) {
        var id = JwtUtils.getUserId(jwt);
        var user = findById(id).orElse(new User());

        user.setId(id);
        user.setUsername(JwtUtils.getUsername(jwt));
        user.setEmail(JwtUtils.getEmail(jwt));
        user.setProvider(provider);

        return userRepository.save(user);
    }
}
