package dev.paw565pl.movie_critics.auth.util;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

public abstract class KeycloakOidcUserUtil {

    public static UUID getUserId(OidcUser oidcUser) {
        return UUID.fromString(oidcUser.getClaimAsString(JwtClaimNames.SUB));
    }

    public static String getUsername(OidcUser oidcUser) {
        return oidcUser.getClaimAsString("preferred_username");
    }

    public static String getEmail(OidcUser oidcUser) {
        return oidcUser.getClaimAsString("email");
    }

    public static Collection<? extends GrantedAuthority> getAuthorities(OidcUser oidcUser) {
        try {
            var realmAccess = oidcUser.getClaimAsMap("realm_access");
            var roles = (List<String>) realmAccess.get("roles");
            return roles.stream()
                    .map((r) -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase()))
                    .toList();
        } catch (IllegalArgumentException | NullPointerException e) {
            return List.of();
        }
    }
}
