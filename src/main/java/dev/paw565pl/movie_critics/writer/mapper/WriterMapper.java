package dev.paw565pl.movie_critics.writer.mapper;

import dev.paw565pl.movie_critics.writer.dto.WriterDto;
import dev.paw565pl.movie_critics.writer.model.WriterEntity;
import dev.paw565pl.movie_critics.writer.response.WriterResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WriterMapper {

    private final ModelMapper modelMapper;

    public WriterMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public WriterEntity toEntity(WriterDto dto) {
        return modelMapper.map(dto, WriterEntity.class);
    }

    public WriterResponse toResponse(WriterEntity writerEntity) {
        return modelMapper.map(writerEntity, WriterResponse.class);
    }
}
