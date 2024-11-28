package dev.paw565pl.movie_critics.movie.controller;

import dev.paw565pl.movie_critics.comment.service.CommentService;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.repository.GenreRepository;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieViewController {

    private final MovieService movieService;
    private final CommentService commentService;
    private final GenreRepository genreRepository;

    public MovieViewController(
            MovieService movieService, CommentService commentService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.commentService = commentService;
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public String redirectToMovieListView() {
        return "redirect:/movies";
    }

    @GetMapping("/movies")
    public String getMovieListView(
            MovieFilterDto filters,
            @RequestParam(required = false) String sort,
            @PageableDefault(size = 50, sort = "ratingsCount", direction = Direction.DESC) Pageable pageable,
            Model model) {
        var movies = movieService.findAll(Optional.empty(), filters, pageable);

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
                new SortOption("Average RatingEntity (Highest)", "averageRating,desc"),
                new SortOption("Average RatingEntity (Lowest)", "averageRating,asc"));
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
    public String getMovieDetailView(@PathVariable Long id, Model model) {
        var movie = movieService.findById(id);
        var movieComments = commentService.findAllByMovieId(id, Pageable.ofSize(20));

        model.addAttribute("movie", movie);
        model.addAttribute("movieComments", movieComments);

        return "movie/movie-detail";
    }
}
