package dev.paw565pl.movie_critics.actor.mapper;

import dev.paw565pl.movie_critics.actor.dto.ActorDto;
import dev.paw565pl.movie_critics.actor.model.ActorEntity;
import dev.paw565pl.movie_critics.actor.response.ActorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    private final ModelMapper modelMapper;

    public ActorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ActorEntity toEntity(ActorDto dto) {
        return modelMapper.map(dto, ActorEntity.class);
    }

    public ActorResponse toResponse(ActorEntity actorEntity) {
        return modelMapper.map(actorEntity, ActorResponse.class);
    }
}
