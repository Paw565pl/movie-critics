package dev.paw565pl.movie_critics.auth;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var authorities =
                Stream.concat(
                                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                                extractRoles(jwt).stream())
                        .toList();

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(@NonNull Jwt jwt) {
        var principalAttribute = "preferred_username";
        var claimName = jwt.getClaimAsString(principalAttribute);

        if (claimName == null) {
            return JwtClaimNames.SUB;
        }

        return claimName;
    }

    private Collection<? extends GrantedAuthority> extractRoles(@NonNull Jwt jwt) {
        try {
            var realmAccess = jwt.getClaimAsMap("realm_access");
            var roles = (List<String>) realmAccess.get("roles");
            return roles.stream().map((r) -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
        } catch (IllegalArgumentException | NullPointerException e) {
            return List.of();
        }
    }
}
