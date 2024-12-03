package dev.paw565pl.movie_critics.movie.controller;

import static dev.paw565pl.movie_critics.auth.util.AuthUtil.hasRole;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.auth.role.Role;
import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.service.CommentService;
import dev.paw565pl.movie_critics.favorite_movie.dto.FavoriteMovieDto;
import dev.paw565pl.movie_critics.favorite_movie.service.FavoriteMovieService;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.repository.GenreRepository;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.movie_to_ignore.dto.MovieToIgnoreDto;
import dev.paw565pl.movie_critics.movie_to_ignore.service.MovieToIgnoreService;
import dev.paw565pl.movie_critics.movie_to_watch.dto.MovieToWatchDto;
import dev.paw565pl.movie_critics.movie_to_watch.service.MovieToWatchService;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.service.RatingService;
import dev.paw565pl.movie_critics.top_active_user.service.TopActiveUserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class MovieViewController {

    private final MovieService movieService;
    private final CommentService commentService;
    private final GenreRepository genreRepository;
    private final RatingService ratingService;
    private final MovieToWatchService movieToWatchService;
    private final FavoriteMovieService favoriteMovieService;
    private final MovieToIgnoreService movieToIgnoreService;
    private final TopActiveUserService topActiveUserService;

    public MovieViewController(
            MovieService movieService,
            CommentService commentService,
            GenreRepository genreRepository,
            RatingService ratingService,
            MovieToWatchService movieToWatchService,
            FavoriteMovieService favoriteMovieService,
            MovieToIgnoreService movieToIgnoreService,
            TopActiveUserService topActiveUserService) {
        this.movieService = movieService;
        this.commentService = commentService;
        this.genreRepository = genreRepository;
        this.ratingService = ratingService;
        this.movieToWatchService = movieToWatchService;
        this.favoriteMovieService = favoriteMovieService;
        this.movieToIgnoreService = movieToIgnoreService;
        this.topActiveUserService = topActiveUserService;
    }

    @GetMapping
    public String redirectToMovieListView() {
        return "redirect:/movies";
    }

    @GetMapping("/movies")
    public String getMovieListView(
            @AuthenticationPrincipal OidcUser oidcUser,
            MovieFilterDto filters,
            @RequestParam(required = false) String sort,
            @PageableDefault(size = 50, sort = "ratingsCount", direction = Direction.DESC) Pageable pageable,
            Model model) {
        var movies = movieService.findAll(
                Optional.ofNullable(oidcUser).map(UserDetailsImpl::fromOidcUser), filters, pageable);

        var ageRatings = movieService.findDistinctAgeRatings();
        var selectedAgeRating = filters.ageRating();

        var genres = genreRepository.findAll();
        var selectedGenreFilterId =
                filters.genreIds() != null ? filters.genreIds().getFirst() : null;
        var selectedGenreFilter = selectedGenreFilterId != null
                ? genreRepository.findById(selectedGenreFilterId).orElse(null)
                : null;

        record SortOption(String label, String value) {}
        var sortOptions = List.of(
                new SortOption("Title (A-Z)", "title,asc"),
                new SortOption("Title (Z-A)", "title,desc"),
                new SortOption("Year (Newest)", "year,desc"),
                new SortOption("Year (Oldest)", "year,asc"),
                new SortOption("Metascore (Highest)", "metaScore,desc"),
                new SortOption("Metascore (Lowest)", "metaScore,asc"),
                new SortOption("Ratings (Most)", "ratingsCount,desc"),
                new SortOption("Ratings (Least)", "ratingsCount,asc"),
                new SortOption("Average Rating (Highest)", "averageRating,desc"),
                new SortOption("Average Rating (Lowest)", "averageRating,asc"));
        var selectedSortOption = sortOptions.stream()
                .filter((option) -> option.value().equals(sort))
                .findFirst()
                .orElse(null);

        model.addAttribute("movies", movies);

        model.addAttribute("ageRatings", ageRatings);
        model.addAttribute("selectedAgeRating", selectedAgeRating);

        model.addAttribute("genres", genres);
        model.addAttribute("selectedGenreFilter", selectedGenreFilter);

        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("selectedSortOption", selectedSortOption);

        return "movie/movie-list";
    }

    @GetMapping("/movies/{id}")
    public String getMovieDetailView(@PathVariable Long id, @AuthenticationPrincipal OidcUser oidcUser, Model model) {
        var movie = movieService.findById(id);
        var movieComments = commentService.findAllByMovieId(id, Pageable.ofSize(20));

        model.addAttribute("movie", movie);
        model.addAttribute("movieComments", movieComments);
        model.addAttribute("newComment", new CommentDto(""));

        if (oidcUser != null) {
            var user = UserDetailsImpl.fromOidcUser(oidcUser);
            var isAdmin = hasRole(user.getAuthorities(), Role.ADMIN);

            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin);

            try {
                var userMovieRatingValue =
                        ratingService.findByMovieIdAndUserId(id, user).value();
                model.addAttribute("userMovieRatingValue", userMovieRatingValue);
            } catch (ResponseStatusException ignored) {
            }

            try {
                var isMovieInToWatchList = movieToWatchService.findByMovieIdAndUserId(id, user) != null;
                model.addAttribute("isMovieInToWatchList", isMovieInToWatchList);
            } catch (ResponseStatusException ignored) {
            }

            try {
                var isFavoriteMovie = favoriteMovieService.findByMovieIdAndUserId(id, user) != null;
                model.addAttribute("isFavoriteMovie", isFavoriteMovie);
            } catch (ResponseStatusException ignored) {
            }

            try {
                var isMovieInToIgnoreList = movieToIgnoreService.findByMovieIdAndUserId(id, user) != null;
                model.addAttribute("isMovieInToIgnoreList", isMovieInToIgnoreList);
            } catch (ResponseStatusException ignored) {
            }
        }

        return "movie/movie-detail";
    }

    @IsAuthenticated
    @PostMapping("/movies/{id}/rate")
    public String rateMovie(
            @PathVariable Long id, @AuthenticationPrincipal OidcUser oidcUser, @RequestParam Byte value, Model model) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);

        try {
            var userMovieRatingValue =
                    ratingService.findByMovieIdAndUserId(id, user).value();

            if (userMovieRatingValue.equals(value)) {
                ratingService.delete(id, user);
            } else {
                ratingService.update(id, user, new RatingDto(value));
            }

            model.addAttribute("userMovieRatingValue", userMovieRatingValue);
        } catch (ResponseStatusException ignored) {
            var userMovieRatingValue =
                    ratingService.create(id, user, new RatingDto(value)).value();
            model.addAttribute("userMovieRatingValue", userMovieRatingValue);
        }

        return "redirect:/movies/{id}";
    }

    @IsAuthenticated
    @PostMapping("/movies/{id}/comments")
    public String addComment(
            @PathVariable Long id,
            @AuthenticationPrincipal OidcUser oidcUser,
            @Valid @ModelAttribute("newComment") CommentDto commentDto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var movie = movieService.findById(id);
            var movieComments = commentService.findAllByMovieId(id, Pageable.ofSize(20));

            model.addAttribute("movie", movie);
            model.addAttribute("movieComments", movieComments);

            return "movie/movie-detail";
        }

        var user = UserDetailsImpl.fromOidcUser(oidcUser);
        try {
            commentService.create(id, user, commentDto);
        } catch (DataIntegrityViolationException e) {
            var movie = movieService.findById(id);
            var movieComments = commentService.findAllByMovieId(id, Pageable.ofSize(20));

            model.addAttribute("movie", movie);
            model.addAttribute("movieComments", movieComments);

            bindingResult.rejectValue("text", "", e.getMessage());

            return "movie/movie-detail";
        }

        return "redirect:/movies/{id}";
    }

    @IsAuthenticated
    @PostMapping("/movies/{movieId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long movieId, @PathVariable Long commentId, @AuthenticationPrincipal OidcUser oidcUser) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);
        try {
            commentService.delete(commentId, movieId, user);
        } catch (ResponseStatusException ignored) {
        }

        return "redirect:/movies/{movieId}";
    }

    @IsAuthenticated
    @PostMapping("/movies/{id}/to-watch")
    public String toggleMovieToWatch(
            @PathVariable Long id,
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestParam(defaultValue = "/movies/{id}") String redirectUrl) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);

        try {
            movieToWatchService.create(user, new MovieToWatchDto(id));
        } catch (DataIntegrityViolationException e) {
            movieToWatchService.delete(id, user);
        }

        return "redirect:" + redirectUrl;
    }

    @IsAuthenticated
    @PostMapping("/movies/{id}/favorite")
    public String toggleFavoriteMovie(
            @PathVariable Long id,
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestParam(defaultValue = "/movies/{id}") String redirectUrl) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);

        try {
            favoriteMovieService.create(user, new FavoriteMovieDto(id));
        } catch (DataIntegrityViolationException e) {
            favoriteMovieService.delete(id, user);
        }

        return "redirect:" + redirectUrl;
    }

    @IsAuthenticated
    @PostMapping("/movies/{id}/ignore")
    public String toggleMovieToIgnoreList(
            @PathVariable Long id,
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestParam(defaultValue = "/movies") String redirectUrl) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);

        try {
            movieToIgnoreService.create(user, new MovieToIgnoreDto(id));
        } catch (DataIntegrityViolationException e) {
            movieToIgnoreService.delete(id, user);
        }

        return "redirect:" + redirectUrl;
    }

    @IsAdmin
    @PostMapping("/movies/{id}/delete")
    public String deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/movies";
    }

    @GetMapping("/hall-of-fame")
    public String getHallOfFameView(Model model) {
        var emptyFilters = new MovieFilterDto(null, null, null, null, null, null, null, null, null, null);
        var movies = movieService.findAll(
                Optional.empty(), emptyFilters, PageRequest.of(0, 10, Direction.DESC, "ratingsCount"));
        var users = topActiveUserService.findTop10ActiveUsers();

        model.addAttribute("movies", movies);
        model.addAttribute("users", users);

        return "movie/hall-of-fame";
    }
}
