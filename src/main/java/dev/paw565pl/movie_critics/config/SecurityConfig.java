package dev.paw565pl.movie_critics.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import dev.paw565pl.movie_critics.auth.KeycloakJwtConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());

        http.oauth2ResourceServer(
                (oauth2) ->
                        oauth2.jwt(
                                (jwt) ->
                                        jwt.jwtAuthenticationConverter(
                                                new KeycloakJwtConverter())));

        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) -> session.sessionCreationPolicy(STATELESS));

        return http.build();
    }
}
