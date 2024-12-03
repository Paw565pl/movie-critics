package dev.paw565pl.movie_critics.movie.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.dto.MovieFilterDto;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Page<MovieResponse> findAll(@AuthenticationPrincipal Jwt jwt, MovieFilterDto filters, Pageable pageable) {
        return movieService.findAll(Optional.ofNullable(jwt).map(UserDetailsImpl::fromJwt), filters, pageable);
    }

    @IsAdmin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@Valid @RequestBody MovieDto dto) {
        return movieService.create(dto, null);
    }

    @GetMapping("/{id}")
    public MovieResponse findById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @IsAdmin
    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieDto dto) {
        return movieService.update(id, dto, null);
    }

    @IsAdmin
    @PatchMapping(path = "/{id}/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieResponse updatePoster(@PathVariable Long id, @RequestParam(value = "poster") MultipartFile poster) {
        return movieService.updatePoster(id, poster);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/poster")
    public void deletePoster(@PathVariable Long id) {
        movieService.deletePoster(id);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }
}
