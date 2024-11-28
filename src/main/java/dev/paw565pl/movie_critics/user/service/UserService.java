package dev.paw565pl.movie_critics.user.service;

import dev.paw565pl.movie_critics.auth.utils.KeycloakJwtUtils;
import dev.paw565pl.movie_critics.auth.utils.KeycloakOidcUserUtils;
import dev.paw565pl.movie_critics.user.model.OAuthProvider;
import dev.paw565pl.movie_critics.user.model.UserEntity;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @Transactional
    public UserEntity createOrUpdate(Jwt jwt, OAuthProvider provider) {
        var id = KeycloakJwtUtils.getUserId(jwt);
        var user = userRepository.findById(id).orElse(new UserEntity());

        user.setId(id);
        user.setUsername(KeycloakJwtUtils.getUsername(jwt));
        user.setEmail(KeycloakJwtUtils.getEmail(jwt));
        user.setProvider(provider);

        return userRepository.save(user);
    }

    @Transactional
    public UserEntity createOrUpdate(OidcUser oidcUser, OAuthProvider provider) {
        var id = KeycloakOidcUserUtils.getUserId(oidcUser);
        var user = userRepository.findById(id).orElse(new UserEntity());

        user.setId(id);
        user.setUsername(KeycloakOidcUserUtils.getUsername(oidcUser));
        user.setEmail(KeycloakOidcUserUtils.getEmail(oidcUser));
        user.setProvider(provider);

        return userRepository.save(user);
    }
}
