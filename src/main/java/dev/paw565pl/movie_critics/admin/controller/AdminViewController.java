package dev.paw565pl.movie_critics.admin.controller;

import dev.paw565pl.movie_critics.actor.dto.ActorDto;
import dev.paw565pl.movie_critics.actor.model.ActorEntity;
import dev.paw565pl.movie_critics.actor.repository.ActorRepository;
import dev.paw565pl.movie_critics.actor.service.ActorService;
import dev.paw565pl.movie_critics.admin.service.AdminService;
import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.director.dto.DirectorDto;
import dev.paw565pl.movie_critics.director.model.DirectorEntity;
import dev.paw565pl.movie_critics.director.repository.DirectorRepository;
import dev.paw565pl.movie_critics.director.service.DirectorService;
import dev.paw565pl.movie_critics.genre.dto.GenreDto;
import dev.paw565pl.movie_critics.genre.model.GenreEntity;
import dev.paw565pl.movie_critics.genre.repository.GenreRepository;
import dev.paw565pl.movie_critics.genre.service.GenreService;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFormDto;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.user.service.UserService;
import dev.paw565pl.movie_critics.writer.model.WriterEntity;
import dev.paw565pl.movie_critics.writer.repository.WriterRepository;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final MovieService movieService;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final WriterRepository writerRepository;
    private final ActorRepository actorRepository;
    private final UserService userService;
    private final AdminService adminService;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final GenreService genreService;

    public AdminViewController(
            MovieService movieService,
            GenreRepository genreRepository,
            DirectorRepository directorRepository,
            WriterRepository writerRepository,
            ActorRepository actorRepository,
            UserService userService,
            AdminService adminService,
            ActorService actorService,
            DirectorService directorService,
            GenreService genreService) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
        this.directorRepository = directorRepository;
        this.writerRepository = writerRepository;
        this.actorRepository = actorRepository;
        this.userService = userService;
        this.adminService = adminService;
        this.actorService = actorService;
        this.directorService = directorService;
        this.genreService = genreService;
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
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
            @RequestParam("poster") MultipartFile poster,
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
                var createdMovie = movieService.create(movieDto, poster);
                return "redirect:/movies/" + createdMovie.getId();
            } else {
                var updatedMovie = movieService.update(editedMovieId, movieDto, poster);
                return "redirect:/movies/" + updatedMovie.getId();
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("title", "", e.getMessage());
            model.addAttribute("editedMovieId", editedMovieId);
            populateMovieForm(model);
            return "admin/movie-form";
        } catch (ResponseStatusException e) {
            populateMovieForm(model);
            model.addAttribute("posterError", e.getReason());
            return "admin/movie-form";
        }
    }

    @IsAdmin
    @GetMapping("/movies/import-export")
    public String getMoviesImportExportView() {
        return "admin/movies-import-export";
    }

    @IsAdmin
    @GetMapping("/movies/export")
    public ResponseEntity<ByteArrayResource> exportMovies() {
        var file = adminService.exportMoviesToJson();
        var formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        var fileName = "movies_" + formattedDateTime + ".json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_JSON)
                .body(file);
    }

    @IsAdmin
    @PostMapping("/movies/import")
    public String importMovies(@RequestParam("moviesJsonFile") MultipartFile moviesJsonFile, Model model) {
        try {
            adminService.importMoviesFromJson(moviesJsonFile);
            return "redirect:/admin/movies/import-export?importSuccess=true";
        } catch (RuntimeException e) {
            String message;
            if (e instanceof ResponseStatusException responseStatusException) {
                message = responseStatusException.getReason();
            } else {
                message = e.getMessage();
            }

            model.addAttribute("moviesImportError", message);
            return "admin/movies-import-export";
        }
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

    private void populateActorView(Model model) {
        model.addAttribute("entites", actorRepository.findAll());
        model.addAttribute("createActionUrl", "/admin/actors/create");
        model.addAttribute("entityName", "actors");
    }

    @IsAdmin
    @GetMapping("/actors")
    public String getActorsView(Model model) {
        populateActorView(model);
        model.addAttribute("formData", new ActorDto(""));
        return "admin/list-group";
    }

    @IsAdmin
    @PostMapping("/actors/create")
    public String createActor(
            @Valid @ModelAttribute("formData") ActorDto actorDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populateActorView(model);
            return "admin/list-group";
        }

        try {
            actorService.create(actorDto);
            return "redirect:/admin/actors";
        } catch (DataIntegrityViolationException e) {
            populateActorView(model);
            bindingResult.rejectValue("name", "", e.getMessage());
            return "admin/list-group";
        }
    }

    @IsAdmin
    @PostMapping("/actors/{id}/delete")
    public String deleteActor(@PathVariable Long id, Model model) {
        try {
            actorService.deleteById(id);
            return "redirect:/admin/actors";
        } catch (DataIntegrityViolationException e) {
            populateActorView(model);
            model.addAttribute("formData", new ActorDto(""));
            model.addAttribute("deleteError", e.getMessage());
            return "admin/list-group";
        }
    }

    private void populateDirectorView(Model model) {
        model.addAttribute("entites", directorRepository.findAll());
        model.addAttribute("createActionUrl", "/admin/directors/create");
        model.addAttribute("entityName", "directors");
    }

    @IsAdmin
    @GetMapping("/directors")
    public String getDirectorsView(Model model) {
        populateDirectorView(model);
        model.addAttribute("formData", new DirectorDto(""));
        return "admin/list-group";
    }

    @IsAdmin
    @PostMapping("/directors/create")
    public String createDirector(
            @Valid @ModelAttribute("formData") DirectorDto directorDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populateDirectorView(model);
            return "admin/list-group";
        }

        try {
            directorService.create(directorDto);
            return "redirect:/admin/directors";
        } catch (DataIntegrityViolationException e) {
            populateDirectorView(model);
            bindingResult.rejectValue("name", "", e.getMessage());
            return "admin/list-group";
        }
    }

    @IsAdmin
    @PostMapping("/directors/{id}/delete")
    public String deleteDirector(@PathVariable Long id, Model model) {
        try {
            directorService.deleteById(id);
            return "redirect:/admin/directors";
        } catch (DataIntegrityViolationException e) {
            populateDirectorView(model);
            model.addAttribute("formData", new DirectorDto(""));
            model.addAttribute("deleteError", e.getMessage());
            return "admin/list-group";
        }
    }

    private void populateGenreView(Model model) {
        model.addAttribute("entites", genreRepository.findAll());
        model.addAttribute("createActionUrl", "/admin/genres/create");
        model.addAttribute("entityName", "genres");
    }

    @IsAdmin
    @GetMapping("/genres")
    public String getGenresView(Model model) {
        populateGenreView(model);
        model.addAttribute("formData", new GenreDto(""));
        return "admin/list-group";
    }

    @IsAdmin
    @PostMapping("/genres/create")
    public String createGenre(
            @Valid @ModelAttribute("formData") GenreDto genreDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populateGenreView(model);
            return "admin/list-group";
        }

        try {
            genreService.create(genreDto);
            return "redirect:/admin/genres";
        } catch (DataIntegrityViolationException e) {
            populateGenreView(model);
            bindingResult.rejectValue("name", "", e.getMessage());
            return "admin/list-group";
        }
    }

    @IsAdmin
    @PostMapping("/genres/{id}/delete")
    public String deleteGenre(@PathVariable Long id, Model model) {
        try {
            genreService.deleteById(id);
            return "redirect:/admin/directors";
        } catch (DataIntegrityViolationException e) {
            populateGenreView(model);
            model.addAttribute("formData", new DirectorDto(""));
            model.addAttribute("deleteError", e.getMessage());
            return "admin/list-group";
        }
    }
}
