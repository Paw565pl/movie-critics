package dev.paw565pl.movie_critics.actor.service;

import dev.paw565pl.movie_critics.actor.dto.ActorDto;
import dev.paw565pl.movie_critics.actor.mapper.ActorMapper;
import dev.paw565pl.movie_critics.actor.model.ActorEntity;
import dev.paw565pl.movie_critics.actor.repository.ActorRepository;
import dev.paw565pl.movie_critics.actor.response.ActorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    private ActorEntity findEntity(Long id) {
        return actorRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor with given id does not exist."));
    }

    public Page<ActorResponse> findAll(Pageable pageable) {
        return actorRepository.findAll(pageable).map(actorMapper::toResponse);
    }

    public ActorResponse findById(Long id) {
        var actorEntity = findEntity(id);
        return actorMapper.toResponse(actorEntity);
    }

    @Transactional
    public ActorResponse create(ActorDto actorDto) {
        var actorEntity = actorMapper.toEntity(actorDto);

        try {
            var savedActorEntity = actorRepository.saveAndFlush(actorEntity);
            return actorMapper.toResponse(savedActorEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Actor with given name already exists.");
        }
    }

    @Transactional
    public ActorResponse update(Long id, ActorDto actorDto) {
        var actorEntity = findEntity(id);

        var updatedActorEntity = actorMapper.toEntity(actorDto);
        updatedActorEntity.setId(actorEntity.getId());

        try {
            var savedActorEntity = actorRepository.saveAndFlush(updatedActorEntity);
            return actorMapper.toResponse(savedActorEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Actor with given name already exists.");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var actorEntity = findEntity(id);

        try {
            actorRepository.deleteById(actorEntity.getId());
            actorRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Actor with given id is associated with one or more movies.");
        }
    }
}
