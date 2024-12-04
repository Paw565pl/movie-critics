package dev.paw565pl.movie_critics.genre.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.genre.dto.GenreDto;
import dev.paw565pl.movie_critics.genre.response.GenreResponse;
import dev.paw565pl.movie_critics.genre.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreRestController {

    private final GenreService genreService;

    public GenreRestController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Page<GenreResponse> findAll(Pageable pageable) {
        return genreService.findAll(pageable);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GenreResponse create(@Valid @RequestBody GenreDto genreDto) {
        return genreService.create(genreDto);
    }

    @GetMapping("/{id}")
    public GenreResponse findById(@PathVariable Long id) {
        return genreService.findById(id);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        genreService.deleteById(id);
    }
}
