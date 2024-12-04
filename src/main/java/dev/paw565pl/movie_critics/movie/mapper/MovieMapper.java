package dev.paw565pl.movie_critics.movie.mapper;

import dev.paw565pl.movie_critics.actor.repository.ActorRepository;
import dev.paw565pl.movie_critics.genre.repository.GenreRepository;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.DirectorRepository;
import dev.paw565pl.movie_critics.movie.repository.WriterRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import java.util.ArrayList;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MovieMapper {

    private final ModelMapper modelMapper;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final WriterRepository writerRepository;

    public MovieMapper(
            ModelMapper modelMapper,
            ActorRepository actorRepository,
            DirectorRepository directorRepository,
            GenreRepository genreRepository,
            WriterRepository writerRepository) {
        this.modelMapper = modelMapper;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
        this.genreRepository = genreRepository;
        this.writerRepository = writerRepository;
    }

    public MovieEntity toEntity(MovieDto dto) {
        var movie = modelMapper.map(dto, MovieEntity.class);

        var actorIds = dto.actorIds() != null ? dto.actorIds() : new ArrayList<Long>();
        var actors = actorRepository.findAllById(actorIds);
        if (actors.size() != actorIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid actor ids.");
        }

        var directorIds = dto.directorIds() != null ? dto.directorIds() : new ArrayList<Long>();
        var directors = directorRepository.findAllById(directorIds);
        if (directors.size() != directorIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid director ids.");
        }

        var genres = genreRepository.findAllById(dto.genreIds());
        if (genres.size() != dto.genreIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid genre ids.");
        }

        var writerIds = dto.writerIds() != null ? dto.writerIds() : new ArrayList<Long>();
        var writers = writerRepository.findAllById(writerIds);
        if (writers.size() != writerIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid writer ids.");
        }

        movie.setActors(actors);
        movie.setDirectors(directors);
        movie.setGenres(genres);
        movie.setWriters(writers);

        return movie;
    }

    public MovieResponse toResponse(MovieEntity movieEntity) {
        return modelMapper.map(movieEntity, MovieResponse.class);
    }
}
