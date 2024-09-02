package dev.paw565pl.movie_critics.movie.mapper;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.movie.repository.*;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import java.util.ArrayList;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements MovieMapper {

    private final ModelMapper modelMapper;

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final WriterRepository writerRepository;

    public MovieMapperImpl(
            ModelMapper modelMapper,
            MovieRepository movieRepository,
            ActorRepository actorRepository,
            DirectorRepository directorRepository,
            GenreRepository genreRepository,
            WriterRepository writerRepository) {
        this.modelMapper = modelMapper;
        this.movieRepository = movieRepository;
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
        var response = modelMapper.map(movie, MovieResponse.class);

        var ratingsCount = movieRepository.countRatingsByMovieId(movie.getId());
        var averageRating =
                movieRepository
                        .findAverageRatingByMovieId(movie.getId())
                        .map((value) -> Math.round(value * 100.0) / 100.0)
                        .orElse(null);

        response.setRatingsCount(ratingsCount);
        response.setAverageRating(averageRating);

        return response;
    }
}
