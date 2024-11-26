package dev.paw565pl.movie_critics.user.service;

import dev.paw565pl.movie_critics.auth.utils.JwtUtils;
import dev.paw565pl.movie_critics.user.model.OAuthProvider;
import dev.paw565pl.movie_critics.user.model.UserEntity;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity createOrUpdate(Jwt jwt, OAuthProvider provider) {
        var id = JwtUtils.getUserId(jwt);
        var user = userRepository.findById(id).orElse(new UserEntity());

        user.setId(id);
        user.setUsername(JwtUtils.getUsername(jwt));
        user.setEmail(JwtUtils.getEmail(jwt));
        user.setProvider(provider);

        return userRepository.save(user);
    }
}
