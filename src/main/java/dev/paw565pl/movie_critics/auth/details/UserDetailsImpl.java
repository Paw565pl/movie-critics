package dev.paw565pl.movie_critics.auth.details;

import dev.paw565pl.movie_critics.auth.util.KeycloakJwtUtil;
import dev.paw565pl.movie_critics.auth.util.KeycloakOidcUserUtil;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;

public class UserDetailsImpl implements UserDetails {

    @Getter
    private final UUID id;

    private final String username;

    @Getter
    private final String email;

    private final Collection<? extends GrantedAuthority> authorities;

    private UserDetailsImpl(
            UUID id, String username, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.email = email;
    }

    public static UserDetailsImpl fromJwt(Jwt jwt) {
        var id = KeycloakJwtUtil.getUserId(jwt);
        var username = KeycloakJwtUtil.getUsername(jwt);
        var email = KeycloakJwtUtil.getEmail(jwt);
        var authorities = KeycloakJwtUtil.getAuthorities(jwt);

        return new UserDetailsImpl(id, username, email, authorities);
    }

    public static UserDetailsImpl fromOidcUser(OidcUser oidcUser) {
        var id = KeycloakOidcUserUtil.getUserId(oidcUser);
        var username = KeycloakOidcUserUtil.getUsername(oidcUser);
        var email = KeycloakOidcUserUtil.getEmail(oidcUser);
        var authorities = KeycloakOidcUserUtil.getAuthorities(oidcUser);

        return new UserDetailsImpl(id, username, email, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
