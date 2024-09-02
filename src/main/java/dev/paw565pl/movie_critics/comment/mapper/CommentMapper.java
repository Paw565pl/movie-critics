package dev.paw565pl.movie_critics.comment.mapper;

import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.model.Comment;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.movie.model.Movie;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final ModelMapper modelMapper;

    public CommentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Comment toEntity(CommentDto dto, Movie movie, UUID userId, String username) {
        return new Comment(dto.text(), username, userId, movie);
    }

    public CommentResponse toResponse(Comment comment) {
        return modelMapper.map(comment, CommentResponse.class);
    }
}
