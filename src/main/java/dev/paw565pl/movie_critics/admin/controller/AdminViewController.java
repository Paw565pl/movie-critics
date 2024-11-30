package dev.paw565pl.movie_critics.admin.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFormDto;
import dev.paw565pl.movie_critics.movie.repository.ActorRepository;
import dev.paw565pl.movie_critics.movie.repository.DirectorRepository;
import dev.paw565pl.movie_critics.movie.repository.GenreRepository;
import dev.paw565pl.movie_critics.movie.repository.WriterRepository;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final MovieService movieService;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final WriterRepository writerRepository;
    private final ActorRepository actorRepository;

    public AdminViewController(
            MovieService movieService,
            GenreRepository genreRepository,
            DirectorRepository directorRepository,
            WriterRepository writerRepository,
            ActorRepository actorRepository) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
        this.directorRepository = directorRepository;
        this.writerRepository = writerRepository;
        this.actorRepository = actorRepository;
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
        model.addAttribute(
                "movieFormData",
                new MovieFormDto(
                        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                        null));

        return "admin/movie-form";
    }

    @IsAdmin
    @PostMapping("/movie/save")
    public String saveMovie(
            @Valid @ModelAttribute("movieFormData") MovieFormDto movieFormDto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            populateMovieForm(model);
            return "admin/admin-panel";
        }

        try {
            var movieDto = new MovieDto(
                    movieFormDto.title(),
                    movieFormDto.ageRating(),
                    movieFormDto.released(),
                    movieFormDto.runtime(),
                    movieFormDto.plot(),
                    movieFormDto.language(),
                    movieFormDto.country(),
                    movieFormDto.awards(),
                    movieFormDto.poster(),
                    movieFormDto.metaScore(),
                    movieFormDto.dvd(),
                    movieFormDto.boxOffice(),
                    movieFormDto.website(),
                    movieFormDto.genreIds(),
                    movieFormDto.directorIds(),
                    movieFormDto.writerIds(),
                    movieFormDto.actorIds());
            movieService.create(movieDto);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("title", "", e.getMessage());
            populateMovieForm(model);
            return "admin/admin-panel";
        }

        return "redirect:/admin";
    }
}
