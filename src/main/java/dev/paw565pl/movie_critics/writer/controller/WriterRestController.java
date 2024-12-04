package dev.paw565pl.movie_critics.writer.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.writer.dto.WriterDto;
import dev.paw565pl.movie_critics.writer.response.WriterResponse;
import dev.paw565pl.movie_critics.writer.service.WriterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/writers")
public class WriterRestController {

    private final WriterService writerService;

    public WriterRestController(WriterService writerService) {
        this.writerService = writerService;
    }

    @GetMapping
    public Page<WriterResponse> findAll(Pageable pageable) {
        return writerService.findAll(pageable);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public WriterResponse create(@Valid @RequestBody WriterDto writerDto) {
        return writerService.create(writerDto);
    }

    @GetMapping("/{id}")
    public WriterResponse findById(@PathVariable Long id) {
        return writerService.findById(id);
    }

    @IsAdmin
    @PutMapping("/{id}")
    public WriterResponse update(@PathVariable Long id, @Valid @RequestBody WriterDto writerDto) {
        return writerService.update(id, writerDto);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        writerService.deleteById(id);
    }
}
