package dev.paw565pl.movie_critics.user.mapper;

import dev.paw565pl.movie_critics.user.model.UserEntity;
import dev.paw565pl.movie_critics.user.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserResponse.class);
    }
}
