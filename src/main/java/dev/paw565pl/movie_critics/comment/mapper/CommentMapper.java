package dev.paw565pl.movie_critics.comment.mapper;

import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.user.model.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentMapper {

    private final ModelMapper modelMapper;

    public CommentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CommentEntity toEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, CommentEntity.class);
    }

    public CommentResponse toResponse(CommentEntity commentEntity) {
        var author = Optional.ofNullable(commentEntity.getAuthor()).map(UserEntity::getUsername).orElse(null);

        return new CommentResponse(
                commentEntity.getId(), commentEntity.getText(), author, commentEntity.getCreatedAt());
    }
}
