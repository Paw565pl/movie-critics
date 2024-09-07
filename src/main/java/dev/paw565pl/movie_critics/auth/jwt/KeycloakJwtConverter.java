package dev.paw565pl.movie_critics.auth.jwt;

import java.util.Collection;
import java.util.List;
import static dev.paw565pl.movie_critics.auth.utils.JwtUtils.getAuthorities;

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
                                getAuthorities(jwt).stream())
                        .toList();
        var subject = jwt.getClaimAsString(JwtClaimNames.SUB);

        return new JwtAuthenticationToken(jwt, authorities, subject);
    }
}
