package dev.paw565pl.movie_critics.auth.config;

import dev.paw565pl.movie_critics.auth.jwt.KeycloakJwtConverter;
import dev.paw565pl.movie_critics.auth.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers.withPkce;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final KeycloakJwtConverter keycloakJwtConverter;
    private final CustomOidcUserService customOidcUserService;

    public SecurityConfig(KeycloakJwtConverter keycloakJwtConverter, CustomOidcUserService customOidcUserService) {
        this.keycloakJwtConverter = keycloakJwtConverter;
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain oAuthResourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**").authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());

        http.oauth2ResourceServer(
                (oauth2) ->
                        oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(keycloakJwtConverter)));

        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) -> session.sessionCreationPolicy(STATELESS));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain oAuthLoginSecurityFilterChain(HttpSecurity http, OAuth2AuthorizationRequestResolver pkceResolver, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http.securityMatcher("/**").authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());

        http.oauth2Login((config) ->
                config.loginPage("/login").authorizationEndpoint((authorizationEndpointConfig) ->
                                authorizationEndpointConfig.authorizationRequestResolver(pkceResolver))
                        .userInfoEndpoint(((userInfo) -> userInfo.oidcUserService(customOidcUserService))));

        http.logout((logout) -> {
            logout.logoutUrl("/logout");

            var logoutSuccessHandler =
                    new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
            logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
            logout.logoutSuccessHandler(logoutSuccessHandler);
        });

        return http.build();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver pkceResolver(ClientRegistrationRepository repo) {
        var resolver = new DefaultOAuth2AuthorizationRequestResolver(repo, DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        resolver.setAuthorizationRequestCustomizer(withPkce());
        return resolver;
    }
}
