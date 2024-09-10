package dev.paw565pl.movie_critics.movie.mapper;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.movie.repository.ActorRepository;
import dev.paw565pl.movie_critics.movie.repository.DirectorRepository;
import dev.paw565pl.movie_critics.movie.repository.GenreRepository;
import dev.paw565pl.movie_critics.movie.repository.WriterRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import java.util.ArrayList;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

    public Movie toEntity(MovieDto dto) {
        var movie = modelMapper.map(dto, Movie.class);

        var actorsIds = dto.actorsIds() != null ? dto.actorsIds() : new ArrayList<Long>();
        var actors = actorRepository.findAllById(actorsIds);
        if (actors.size() != actorsIds.size()) {
            throw new IllegalArgumentException("Invalid actors ids.");
        }

        var directorsIds = dto.directorsIds() != null ? dto.directorsIds() : new ArrayList<Long>();
        var directors = directorRepository.findAllById(directorsIds);
        if (directors.size() != directorsIds.size()) {
            throw new IllegalArgumentException("Invalid directors ids.");
        }

        var genres = genreRepository.findAllById(dto.genresIds());
        if (genres.size() != dto.genresIds().size()) {
            throw new IllegalArgumentException("Invalid genres ids.");
        }

        var writersIds = dto.writersIds() != null ? dto.writersIds() : new ArrayList<Long>();
        var writers = writerRepository.findAllById(writersIds);
        if (writers.size() != writersIds.size()) {
            throw new IllegalArgumentException("Invalid writers ids.");
        }

        movie.setActors(actors);
        movie.setDirectors(directors);
        movie.setGenres(genres);
        movie.setWriters(writers);

        return movie;
    }

    public MovieResponse toResponse(Movie movie) {
        return modelMapper.map(movie, MovieResponse.class);
    }
}
