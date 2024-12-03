package dev.paw565pl.movie_critics.auth.jwt;

import static dev.paw565pl.movie_critics.auth.util.KeycloakJwtUtil.getAuthorities;

import dev.paw565pl.movie_critics.user.model.OAuthProvider;
import dev.paw565pl.movie_critics.user.service.UserService;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    private final UserService userService;

    public KeycloakJwtConverter(
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter, UserService userService) {
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
        this.userService = userService;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var authorities = Stream.concat(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(), getAuthorities(jwt).stream())
                .toList();
        var subject = jwt.getClaimAsString(JwtClaimNames.SUB);

        userService.createOrUpdate(jwt, OAuthProvider.KEYCLOAK);

        return new JwtAuthenticationToken(jwt, authorities, subject);
    }
}
