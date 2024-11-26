package dev.paw565pl.movie_critics.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.*;
import dev.paw565pl.movie_critics.movie.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminService {

    private final ObjectMapper objectMapper;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final WriterRepository writerRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public AdminService(
            ObjectMapper objectMapper,
            GenreRepository genreRepository,
            ActorRepository actorRepository,
            DirectorRepository directorRepository,
            WriterRepository writerRepository,
            MovieRepository movieRepository,
            MovieMapper movieMapper) {
        this.objectMapper = objectMapper;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
        this.writerRepository = writerRepository;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public ByteArrayResource createMoviesExportBytes() {
        var movies = movieRepository.findAll().stream().map(movieMapper::toResponse).toList();

        try {
            var moviesBytes = objectMapper.writeValueAsBytes(movies);
            return new ByteArrayResource(moviesBytes);
        } catch (JsonProcessingException e) {
            log.error("Error while exporting movies.", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while exporting movies.");
        }
    }

    @Transactional(rollbackFor = {IOException.class})
    public void importMovies(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File can not be empty.");
        }

        var isJsonFile =
                file.getOriginalFilename().toLowerCase().endsWith(".json")
                        && file.getContentType().equalsIgnoreCase("application/json");
        if (!isJsonFile) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "File must be a JSON file.");
        }

        try (var inputStream = file.getInputStream()) {
            List<Map<String, Object>> jsonList = objectMapper.readValue(inputStream, List.class);

            var genres = new HashSet<GenreEntity>();
            var directors = new HashSet<DirectorEntity>();
            var writers = new HashSet<WriterEntity>();
            var actors = new HashSet<ActorEntity>();
            var movies = new HashSet<MovieEntity>();

            jsonList.forEach(
                    (m) -> {
                        var currentGenres = new HashSet<GenreEntity>();
                        var currentDirectors = new HashSet<DirectorEntity>();
                        var currentWriters = new HashSet<WriterEntity>();
                        var currentActors = new HashSet<ActorEntity>();

                        var genresJson = (List<Map<String, String>>) m.get("genres");
                        genresJson.forEach(
                                (g) -> {
                                    var genreName = g.get("name");
                                    var genre =
                                            genreRepository
                                                    .findByNameIgnoreCase(genreName)
                                                    .orElse(new GenreEntity(genreName));

                                    currentGenres.add(genre);
                                    genres.add(genre);
                                });

                        var directorsJson = (List<Map<String, String>>) m.get("directors");
                        directorsJson.forEach(
                                (d) -> {
                                    var directorName = d.get("name");
                                    var director =
                                            directorRepository
                                                    .findByNameIgnoreCase(directorName)
                                                    .orElse(new DirectorEntity(directorName));

                                    currentDirectors.add(director);
                                    directors.add(director);
                                });

                        var writersJson = (List<Map<String, String>>) m.get("writers");
                        writersJson.forEach(
                                (w) -> {
                                    var writerName = w.get("name");
                                    var writer =
                                            writerRepository
                                                    .findByNameIgnoreCase(writerName)
                                                    .orElse(new WriterEntity(writerName));

                                    currentWriters.add(writer);
                                    writers.add(writer);
                                });

                        var actorsJson = (List<Map<String, String>>) m.get("actors");
                        actorsJson.forEach(
                                (a) -> {
                                    var actorName = a.get("name");
                                    var actor =
                                            actorRepository
                                                    .findByNameIgnoreCase(actorName)
                                                    .orElse(new ActorEntity(actorName));

                                    currentActors.add(actor);
                                    actors.add(actor);
                                });

                        var movie = objectMapper.convertValue(m, MovieEntity.class);

                        movie.setGenres(currentGenres.stream().toList());
                        movie.setDirectors(currentDirectors.stream().toList());
                        movie.setWriters(currentWriters.stream().toList());
                        movie.setActors(currentActors.stream().toList());

                        movies.add(movie);
                    });

            genreRepository.saveAllAndFlush(genres);
            directorRepository.saveAllAndFlush(directors);
            writerRepository.saveAllAndFlush(writers);
            actorRepository.saveAllAndFlush(actors);
            movieRepository.saveAllAndFlush(movies);

        } catch (DataIntegrityViolationException e) {
            log.error("One or more movies already exist.", e);
            throw new DataIntegrityViolationException("One or more movies already exist.");
        } catch (NullPointerException | DatabindException e) {
            log.error("Error while importing movies.", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON file.");
        } catch (IOException e) {
            log.error("Error while importing movies.", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while importing movies.");
        }
    }
}
