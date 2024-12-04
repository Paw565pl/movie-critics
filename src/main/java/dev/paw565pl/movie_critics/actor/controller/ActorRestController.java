package dev.paw565pl.movie_critics.actor.controller;

import dev.paw565pl.movie_critics.actor.dto.ActorDto;
import dev.paw565pl.movie_critics.actor.response.ActorResponse;
import dev.paw565pl.movie_critics.actor.service.ActorService;
import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        actorService.deleteById(id);
    }
}
