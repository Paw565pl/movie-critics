package dev.paw565pl.movie_critics.genre.mapper;

import dev.paw565pl.movie_critics.genre.dto.GenreDto;
import dev.paw565pl.movie_critics.genre.model.GenreEntity;
import dev.paw565pl.movie_critics.genre.response.GenreResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    private final ModelMapper modelMapper;

    public GenreMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GenreEntity toEntity(GenreDto dto) {
        return modelMapper.map(dto, GenreEntity.class);
    }

    public GenreResponse toResponse(GenreEntity genreEntity) {
        return modelMapper.map(genreEntity, GenreResponse.class);
    }
}
