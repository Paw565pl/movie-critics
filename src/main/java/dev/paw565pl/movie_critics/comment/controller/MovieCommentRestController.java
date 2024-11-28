package dev.paw565pl.movie_critics.comment.controller;

import dev.paw565pl.movie_critics.auth.annotation.IsAuthenticated;
import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies/{movieId}/comments")
public class MovieCommentRestController {

    private final CommentService commentService;

    public MovieCommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public Page<CommentResponse> findAllByMovieId(@PathVariable Long movieId, Pageable pageable) {
        return commentService.findAllByMovieId(movieId, pageable);
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(
            @PathVariable Long movieId, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CommentDto dto) {
        return commentService.create(movieId, jwt, dto);
    }

    @GetMapping("/{commentId}")
    public CommentResponse findByIdAndMovieId(@PathVariable Long movieId, @PathVariable Long commentId) {
        return commentService.findByIdAndMovieId(commentId, movieId);
    }

    @IsAuthenticated
    @PutMapping("/{commentId}")
    public CommentResponse update(
            @PathVariable Long movieId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CommentDto dto) {
        return commentService.update(commentId, movieId, jwt, dto);
    }

    @IsAuthenticated
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movieId, @PathVariable Long commentId, @AuthenticationPrincipal Jwt jwt) {
        commentService.delete(commentId, movieId, jwt);
    }
}
