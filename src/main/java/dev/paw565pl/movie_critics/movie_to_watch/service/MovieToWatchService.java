package dev.paw565pl.movie_critics.movie_to_watch.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie_to_watch.dto.MovieToWatchDto;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MovieToWatchService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieToWatchService(
            UserRepository userRepository,
            MovieRepository movieRepository,
            MovieMapper movieMapper) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    private MovieEntity findMovieToWatch(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoWantToWatchId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Page<MovieResponse> findAll(Jwt jwt, Pageable pageable) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        return movieRepository
                .findAllByUsersWhoWantToWatchId(userId, pageable)
                .map(movieMapper::toResponse);
    }

    @Transactional
    public MovieResponse create(Jwt jwt, MovieToWatchDto dto) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var movie =
                movieRepository
                        .findById(dto.movieId())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                new MovieNotFoundException().getMessage()));
        var user = userRepository.findById(userId).orElseThrow();

        try {
            user.getMoviesToWatch().add(movie);
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your to watch list.");
        }

        return movieMapper.toResponse(movie);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var movieToWatch = findMovieToWatch(movieId, userId);

        var user = userRepository.findById(userId).orElseThrow();
        user.getMoviesToWatch().remove(movieToWatch);
        userRepository.save(user);
    }
}
