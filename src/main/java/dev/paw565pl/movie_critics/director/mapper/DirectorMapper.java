package dev.paw565pl.movie_critics.director.mapper;

import dev.paw565pl.movie_critics.director.dto.DirectorDto;
import dev.paw565pl.movie_critics.director.model.DirectorEntity;
import dev.paw565pl.movie_critics.director.response.DirectorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DirectorMapper {

    private final ModelMapper modelMapper;

    public DirectorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DirectorEntity toEntity(DirectorDto dto) {
        return modelMapper.map(dto, DirectorEntity.class);
    }

    public DirectorResponse toResponse(DirectorEntity directorEntity) {
        return modelMapper.map(directorEntity, DirectorResponse.class);
    }
}
