package dev.paw565pl.movieCritics.movie.mapper;

import dev.paw565pl.movieCritics.movie.dto.MovieDto;
import dev.paw565pl.movieCritics.movie.model.Movie;
import dev.paw565pl.movieCritics.movie.repository.ActorRepository;
import dev.paw565pl.movieCritics.movie.repository.DirectorRepository;
import dev.paw565pl.movieCritics.movie.repository.GenreRepository;
import dev.paw565pl.movieCritics.movie.repository.WriterRepository;
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

    var actors = actorRepository.findAllById(dto.getActorsIds());
    if (actors.size() != dto.getActorsIds().size()) {
      throw new IllegalArgumentException("Invalid actors ids.");
    }

    var directors = directorRepository.findAllById(dto.getDirectorsIds());
    if (directors.size() != dto.getDirectorsIds().size()) {
      throw new IllegalArgumentException("Invalid directors ids.");
    }

    var genres = genreRepository.findAllById(dto.getGenresIds());
    if (genres.size() != dto.getGenresIds().size()) {
      throw new IllegalArgumentException("Invalid genres ids.");
    }

    var writers = writerRepository.findAllById(dto.getWritersIds());
    if (writers.size() != dto.getWritersIds().size()) {
      throw new IllegalArgumentException("Invalid writers ids.");
    }

    movie.setActors(actors);
    movie.setDirectors(directors);
    movie.setGenres(genres);
    movie.setWriters(writers);

    return movie;
  }
}
