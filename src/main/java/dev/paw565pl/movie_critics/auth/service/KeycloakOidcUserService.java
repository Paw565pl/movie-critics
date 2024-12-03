package dev.paw565pl.movie_critics.auth.service;

import static dev.paw565pl.movie_critics.auth.util.KeycloakOidcUserUtil.getAuthorities;

import dev.paw565pl.movie_critics.user.model.OAuthProvider;
import dev.paw565pl.movie_critics.user.service.UserService;
import java.util.stream.Stream;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class KeycloakOidcUserService extends OidcUserService {

    private final UserService userService;

    public KeycloakOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        var oidcUser = super.loadUser(oidcUserRequest);
        var authorities = Stream.concat(oidcUser.getAuthorities().stream(), getAuthorities(oidcUser).stream())
                .toList();

        userService.createOrUpdate(oidcUser, OAuthProvider.KEYCLOAK);

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
