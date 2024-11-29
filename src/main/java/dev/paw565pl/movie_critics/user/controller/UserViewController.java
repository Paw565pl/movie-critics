package dev.paw565pl.movie_critics.user.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.favorite_movie.service.FavoriteMovieService;
import dev.paw565pl.movie_critics.movie_to_ignore.service.MovieToIgnoreService;
import dev.paw565pl.movie_critics.movie_to_watch.service.MovieToWatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserViewController {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String keycloakUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String keycloakClientId;

    private final MovieToWatchService movieToWatchService;
    private final FavoriteMovieService favoriteMovieService;
    private final MovieToIgnoreService movieToIgnoreService;

    public UserViewController(
            MovieToWatchService movieToWatchService,
            FavoriteMovieService favoriteMovieService,
            MovieToIgnoreService movieToIgnoreService) {
        this.movieToWatchService = movieToWatchService;
        this.favoriteMovieService = favoriteMovieService;
        this.movieToIgnoreService = movieToIgnoreService;
    }

    @IsAuthenticated
    @GetMapping
    public String getProfileView(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);
        var moviesToWatch = movieToWatchService.findAll(user, Pageable.ofSize(50));
        var favoriteMovies = favoriteMovieService.findAll(user, Pageable.ofSize(50));
        var moviesToIgnore = movieToIgnoreService.findAll(user, Pageable.ofSize(50));
        var keycloakAccountUrl = String.format("%s/account?referrer=%s", keycloakUrl, keycloakClientId);

        model.addAttribute("user", user);
        model.addAttribute("moviesToWatch", moviesToWatch);
        model.addAttribute("favoriteMovies", favoriteMovies);
        model.addAttribute("moviesToIgnore", moviesToIgnore);
        model.addAttribute("keycloakAccountUrl", keycloakAccountUrl);

        return "profile/profile";
    }
}
