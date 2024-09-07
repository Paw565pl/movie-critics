package dev.paw565pl.movie_critics.comment.response;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String text, String author, LocalDateTime createdAt) {}
