package dev.paw565pl.movie_critics.director.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.director.dto.DirectorDto;
import dev.paw565pl.movie_critics.director.response.DirectorResponse;
import dev.paw565pl.movie_critics.director.service.DirectorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/directors")
public class DirectorRestController {

    private final DirectorService directorService;

    public DirectorRestController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Page<DirectorResponse> findAll(Pageable pageable) {
        return directorService.findAll(pageable);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DirectorResponse create(@Valid @RequestBody DirectorDto directorDto) {
        return directorService.create(directorDto);
    }

    @GetMapping("/{id}")
    public DirectorResponse findById(@PathVariable Long id) {
        return directorService.findById(id);
    }

    @IsAdmin
    @PutMapping("/{id}")
    public DirectorResponse update(@PathVariable Long id, @Valid @RequestBody DirectorDto directorDto) {
        return directorService.update(id, directorDto);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        directorService.deleteById(id);
    }
}
