package dev.paw565pl.movie_critics.top_active_user.mapper;

import dev.paw565pl.movie_critics.top_active_user.response.TopActiveUserResponse;
import dev.paw565pl.movie_critics.user.model.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TopActiveUserMapper {

    private final ModelMapper modelMapper;

    public TopActiveUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TopActiveUserResponse toResponse(UserEntity userEntity) {
        return modelMapper.map(userEntity, TopActiveUserResponse.class);
    }
}
