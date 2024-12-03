package dev.paw565pl.movie_critics.movie_to_ignore.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie_to_ignore.dto.MovieToIgnoreDto;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import dev.paw565pl.movie_critics.user.service.UserService;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MovieToIgnoreService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public MovieToIgnoreService(
            MovieRepository movieRepository,
            MovieMapper movieMapper,
            UserRepository userRepository,
            UserService userService) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private MovieEntity findMovieEntity(Long id) {
        return movieRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, new MovieNotFoundException().getMessage()));
    }

    private MovieEntity findMovieToIgnoreEntity(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoIgnoredId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie with given id is not in your ignored list."));
    }

    public Page<MovieResponse> findAll(UserDetailsImpl user, Pageable pageable) {
        var userId = user.getId();
        return movieRepository.findAllByUsersWhoIgnoredId(userId, pageable).map(movieMapper::toResponse);
    }

    public MovieResponse findByMovieIdAndUserId(Long movieId, UserDetailsImpl user) {
        var movieEntity = findMovieToIgnoreEntity(movieId, user.getId());
        return movieMapper.toResponse(movieEntity);
    }

    @Transactional
    public MovieResponse create(UserDetailsImpl user, MovieToIgnoreDto dto) {
        var movieEntity = findMovieEntity(dto.movieId());

        var userId = user.getId();
        var userEntity = userService.findEntityById(userId);

        try {
            userEntity.getIgnoredMovies().add(movieEntity);
            userRepository.saveAndFlush(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your ignored list.");
        }

        return movieMapper.toResponse(movieEntity);
    }

    @Transactional
    public void delete(Long movieId, UserDetailsImpl user) {
        var userId = user.getId();
        var movieToIgnore = findMovieToIgnoreEntity(movieId, userId);

        var userEntity = userService.findEntityById(userId);
        userEntity.getIgnoredMovies().remove(movieToIgnore);

        userRepository.save(userEntity);
    }
}
