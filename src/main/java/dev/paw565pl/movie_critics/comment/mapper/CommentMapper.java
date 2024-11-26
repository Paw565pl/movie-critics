package dev.paw565pl.movie_critics.comment.mapper;

import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.user.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentMapper {

    public CommentEntity toEntity(CommentDto commentDto, User author, MovieEntity movieEntity) {
        var comment = new CommentEntity(commentDto.text(), movieEntity);
        comment.setAuthor(author);

        return comment;
    }

    public CommentResponse toResponse(CommentEntity commentEntity) {
        var author = Optional.ofNullable(commentEntity.getAuthor()).map(User::getUsername).orElse(null);

        return new CommentResponse(
                commentEntity.getId(), commentEntity.getText(), author, commentEntity.getCreatedAt());
    }
}
