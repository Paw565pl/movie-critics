package dev.paw565pl.movie_critics.actor.controller;

import dev.paw565pl.movie_critics.actor.dto.ActorDto;
import dev.paw565pl.movie_critics.actor.response.ActorResponse;
import dev.paw565pl.movie_critics.actor.service.ActorService;
import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorRestController {

    private final ActorService actorService;

    public ActorRestController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public Page<ActorResponse> findAll(Pageable pageable) {
        return actorService.findAll(pageable);
    }

    @IsAdmin
    @PostMapping
    public ActorResponse create(@Valid @RequestBody ActorDto actorDto) {
        return actorService.create(actorDto);
    }

    @GetMapping("/{id}")
    public ActorResponse findById(@PathVariable Long id) {
        return actorService.findById(id);
    }

    @IsAdmin
    @PutMapping("/{id}")
    public ActorResponse update(@PathVariable Long id, @Valid @RequestBody ActorDto actorDto) {
        return actorService.update(id, actorDto);
    }

    @IsAdmin
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        try {
            actorService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Actor with given id is associated with one or more movies.");
        }
    }
}
