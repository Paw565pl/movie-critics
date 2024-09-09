package dev.paw565pl.movie_critics.comment.mapper;

import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.user.model.User;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentDto dto, User author, Movie movie) {
        var comment = new Comment(dto.text(), movie);
        comment.setAuthor(author);
        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        var author = Optional.ofNullable(comment.getAuthor()).map(User::getUsername).orElse(null);

        return new CommentResponse(
                comment.getId(), comment.getText(), author, comment.getCreatedAt());
    }
}
