package dev.paw565pl.movie_critics.comment.mapper;

import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentDto dto, User author, Movie movie) {
        return new Comment(dto.text(), author, movie);
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt());
    }
}
