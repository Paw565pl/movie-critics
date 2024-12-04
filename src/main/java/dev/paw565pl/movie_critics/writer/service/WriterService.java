package dev.paw565pl.movie_critics.writer.service;

import dev.paw565pl.movie_critics.writer.dto.WriterDto;
import dev.paw565pl.movie_critics.writer.mapper.WriterMapper;
import dev.paw565pl.movie_critics.writer.model.WriterEntity;
import dev.paw565pl.movie_critics.writer.repository.WriterRepository;
import dev.paw565pl.movie_critics.writer.response.WriterResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WriterService {

    private final WriterRepository writerRepository;
    private final WriterMapper writerMapper;

    public WriterService(WriterRepository writerRepository, WriterMapper writerMapper) {
        this.writerRepository = writerRepository;
        this.writerMapper = writerMapper;
    }

    private WriterEntity findEntity(Long id) {
        return writerRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Writer with given id does not exist."));
    }

    public Page<WriterResponse> findAll(Pageable pageable) {
        return writerRepository.findAll(pageable).map(writerMapper::toResponse);
    }

    public WriterResponse findById(Long id) {
        var writerEntity = findEntity(id);
        return writerMapper.toResponse(writerEntity);
    }

    @Transactional
    public WriterResponse create(WriterDto writerDto) {
        var writerEntity = writerMapper.toEntity(writerDto);

        try {
            var savedWriterEntity = writerRepository.saveAndFlush(writerEntity);
            return writerMapper.toResponse(savedWriterEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Writer with given name already exists.");
        }
    }

    @Transactional
    public WriterResponse update(Long id, WriterDto writerDto) {
        var writerEntity = findEntity(id);

        var updatedWriterEntity = writerMapper.toEntity(writerDto);
        updatedWriterEntity.setId(writerEntity.getId());

        try {
            var savedWriterEntity = writerRepository.saveAndFlush(updatedWriterEntity);
            return writerMapper.toResponse(savedWriterEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Writer with given name already exists.");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var writerEntity = findEntity(id);

        try {
            writerRepository.deleteById(writerEntity.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Writer with given id is associated with one or more movies.");
        }
    }
}
