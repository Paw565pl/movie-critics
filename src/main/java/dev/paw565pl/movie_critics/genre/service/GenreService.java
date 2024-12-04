package dev.paw565pl.movie_critics.genre.service;

import dev.paw565pl.movie_critics.genre.dto.GenreDto;
import dev.paw565pl.movie_critics.genre.mapper.GenreMapper;
import dev.paw565pl.movie_critics.genre.model.GenreEntity;
import dev.paw565pl.movie_critics.genre.repository.GenreRepository;
import dev.paw565pl.movie_critics.genre.response.GenreResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    private GenreEntity findEntity(Long id) {
        return genreRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre with given id does not exist."));
    }

    public Page<GenreResponse> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable).map(genreMapper::toResponse);
    }

    public GenreResponse findById(Long id) {
        var genreEntity = findEntity(id);
        return genreMapper.toResponse(genreEntity);
    }

    @Transactional
    public GenreResponse create(GenreDto genreDto) {
        var genreEntity = genreMapper.toEntity(genreDto);

        try {
            var savedGenreEntity = genreRepository.saveAndFlush(genreEntity);
            return genreMapper.toResponse(savedGenreEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Genre with given name already exists.");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var genreEntity = findEntity(id);

        try {
            genreRepository.deleteById(genreEntity.getId());
            genreRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Genre with given id is associated with one or more movies.");
        }
    }
}
