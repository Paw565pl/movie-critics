package dev.paw565pl.movie_critics.director.service;

import dev.paw565pl.movie_critics.director.dto.DirectorDto;
import dev.paw565pl.movie_critics.director.mapper.DirectorMapper;
import dev.paw565pl.movie_critics.director.model.DirectorEntity;
import dev.paw565pl.movie_critics.director.repository.DirectorRepository;
import dev.paw565pl.movie_critics.director.response.DirectorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public DirectorService(DirectorRepository directorRepository, DirectorMapper directorMapper) {
        this.directorRepository = directorRepository;
        this.directorMapper = directorMapper;
    }

    private DirectorEntity findEntity(Long id) {
        return directorRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Director with given id does not exist."));
    }

    public Page<DirectorResponse> findAll(Pageable pageable) {
        return directorRepository.findAll(pageable).map(directorMapper::toResponse);
    }

    public DirectorResponse findById(Long id) {
        var directorEntity = findEntity(id);
        return directorMapper.toResponse(directorEntity);
    }

    @Transactional
    public DirectorResponse create(DirectorDto directorDto) {
        var directorEntity = directorMapper.toEntity(directorDto);

        try {
            var savedDirectorEntity = directorRepository.saveAndFlush(directorEntity);
            return directorMapper.toResponse(savedDirectorEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Director with given name already exists.");
        }
    }

    @Transactional
    public DirectorResponse update(Long id, DirectorDto directorDto) {
        var directorEntity = findEntity(id);

        var updatedDirectorEntity = directorMapper.toEntity(directorDto);
        updatedDirectorEntity.setId(directorEntity.getId());

        try {
            var savedDirectorEntity = directorRepository.saveAndFlush(updatedDirectorEntity);
            return directorMapper.toResponse(savedDirectorEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Director with given name already exists.");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var directorEntity = findEntity(id);

        try {
            directorRepository.deleteById(directorEntity.getId());
            directorRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Director with given id is associated with one or more movies.");
        }
    }
}
