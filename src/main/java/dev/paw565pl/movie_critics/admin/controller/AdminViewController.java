package dev.paw565pl.movie_critics.admin.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFormDto;
import dev.paw565pl.movie_critics.movie.model.ActorEntity;
import dev.paw565pl.movie_critics.movie.model.DirectorEntity;
import dev.paw565pl.movie_critics.movie.model.GenreEntity;
import dev.paw565pl.movie_critics.movie.model.WriterEntity;
import dev.paw565pl.movie_critics.movie.repository.ActorRepository;
import dev.paw565pl.movie_critics.movie.repository.DirectorRepository;
import dev.paw565pl.movie_critics.movie.repository.GenreRepository;
import dev.paw565pl.movie_critics.movie.repository.WriterRepository;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.user.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final MovieService movieService;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final WriterRepository writerRepository;
    private final ActorRepository actorRepository;
    private final UserService userService;

    public AdminViewController(
            MovieService movieService,
            GenreRepository genreRepository,
            DirectorRepository directorRepository,
            WriterRepository writerRepository,
            ActorRepository actorRepository,
            UserService userService) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
        this.directorRepository = directorRepository;
        this.writerRepository = writerRepository;
        this.actorRepository = actorRepository;
        this.userService = userService;
    }

    private void populateMovieForm(Model model) {
        var ageRatingOptions = movieService.findDistinctAgeRatings();
        var genreOptions = genreRepository.findAll();
        var directorOptions = directorRepository.findAll();
        var writerOptions = writerRepository.findAll();
        var actorOptions = actorRepository.findAll();

        model.addAttribute("ageRatingOptions", ageRatingOptions);
        model.addAttribute("genreOptions", genreOptions);
        model.addAttribute("directorOptions", directorOptions);
        model.addAttribute("writerOptions", writerOptions);
        model.addAttribute("actorOptions", actorOptions);
    }

    @IsAdmin
    @GetMapping
    public String getAdminPanelView() {
        return "admin/admin-panel";
    }

    @IsAdmin
    @GetMapping("/movie/add")
    public String getNewMovieForm(Model model) {
        populateMovieForm(model);
        var movieFormDto = new MovieFormDto(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        model.addAttribute("movieFormDto", movieFormDto);

        return "admin/movie-form";
    }

    @IsAdmin
    @GetMapping("/movie/{id}/edit")
    public String getMovieEditForm(@PathVariable Long id, Model model) {
        var movieEntity = movieService.findEntity(id);
        var movieFormDto = new MovieFormDto(
                movieEntity.getTitle(),
                movieEntity.getAgeRating(),
                movieEntity.getReleased(),
                movieEntity.getRuntime(),
                movieEntity.getPlot(),
                movieEntity.getLanguage(),
                movieEntity.getCountry(),
                movieEntity.getAwards(),
                movieEntity.getPosterUrl(),
                movieEntity.getMetaScore(),
                movieEntity.getDvd(),
                movieEntity.getBoxOffice(),
                movieEntity.getWebsite(),
                movieEntity.getGenres().stream().map(GenreEntity::getId).toList(),
                movieEntity.getDirectors().stream().map(DirectorEntity::getId).toList(),
                movieEntity.getWriters().stream().map(WriterEntity::getId).toList(),
                movieEntity.getActors().stream().map(ActorEntity::getId).toList());

        populateMovieForm(model);
        model.addAttribute("editedMovieId", id);
        model.addAttribute("movieFormDto", movieFormDto);

        return "admin/movie-form";
    }

    @IsAdmin
    @PostMapping("/movie/save")
    public String saveMovie(
            @RequestParam(value = "editedMovieId", required = false) Long editedMovieId,
            @Valid @ModelAttribute("movieFormDto") MovieFormDto movieFormDto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            populateMovieForm(model);
            model.addAttribute("editedMovieId", editedMovieId);
            return "admin/movie-form";
        }

        var movieDto = new MovieDto(
                movieFormDto.title(),
                movieFormDto.ageRating().isBlank() ? null : movieFormDto.ageRating(),
                movieFormDto.released(),
                movieFormDto.runtime().isBlank() ? null : movieFormDto.runtime(),
                movieFormDto.plot().isBlank() ? null : movieFormDto.plot(),
                movieFormDto.language().isBlank() ? null : movieFormDto.language(),
                movieFormDto.country().isBlank() ? null : movieFormDto.country(),
                movieFormDto.awards().isBlank() ? null : movieFormDto.awards(),
                movieFormDto.posterUrl().isBlank() ? null : movieFormDto.posterUrl(),
                movieFormDto.metaScore(),
                movieFormDto.dvd().isBlank() ? null : movieFormDto.dvd(),
                movieFormDto.boxOffice().isBlank() ? null : movieFormDto.boxOffice(),
                movieFormDto.website().isBlank() ? null : movieFormDto.website(),
                movieFormDto.genreIds(),
                movieFormDto.directorIds(),
                movieFormDto.writerIds(),
                movieFormDto.actorIds());

        try {
            if (editedMovieId == null) {
                var createdMovie = movieService.create(movieDto);
                return "redirect:/movies/" + createdMovie.getId();
            } else {
                var updatedMovie = movieService.update(editedMovieId, movieDto);
                return "redirect:/movies/" + updatedMovie.getId();
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("title", "", e.getMessage());
            model.addAttribute("editedMovieId", editedMovieId);
            populateMovieForm(model);
            return "admin/movie-form";
        }
    }

    @IsAdmin
    @GetMapping("/movies/import-export")
    public String getMoviesImportExportView() {
        return "admin/movies-import-export";
    }

    @IsAdmin
    @GetMapping("/users")
    public String getUserListView(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        var user = UserDetailsImpl.fromOidcUser(oidcUser);
        var users = userService.findAll(Pageable.ofSize(50));

        model.addAttribute("users", users);
        model.addAttribute("currentUsername", user.getUsername());

        return "admin/user-list";
    }

    @IsAdmin
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
