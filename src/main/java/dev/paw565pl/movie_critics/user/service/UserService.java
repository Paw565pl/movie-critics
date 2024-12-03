package dev.paw565pl.movie_critics.user.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.user.mapper.UserMapper;
import dev.paw565pl.movie_critics.user.model.OAuthProvider;
import dev.paw565pl.movie_critics.user.model.UserEntity;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import dev.paw565pl.movie_critics.user.response.UserResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    public UserResponse findById(UUID id) {
        return userRepository
                .findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id does not exist."));
    }

    public UserEntity findEntityById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @Transactional
    public UserEntity createOrUpdate(Jwt jwt, OAuthProvider provider) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var userId = user.getId();

        var userEntity = userRepository.findById(userId).orElse(new UserEntity());

        userEntity.setId(userId);
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setProvider(provider);

        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity createOrUpdate(OidcUser oidcUser, OAuthProvider provider) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);
        var userId = user.getId();

        var userEntity = userRepository.findById(userId).orElse(new UserEntity());

        userEntity.setId(userId);
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setProvider(provider);

        return userRepository.save(userEntity);
    }

    @Transactional
    public void deleteById(UUID id) {
        var userResponse = findById(id);
        userRepository.deleteById(userResponse.getId());
    }
}
