package dev.paw565pl.movie_critics.movie_to_watch.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.movie_to_watch.dto.MovieToWatchDto;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class MovieToWatchService {

    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final UserRepository userRepository;

    public MovieToWatchService(MovieRepository movieRepository, MovieService movieService, MovieMapper movieMapper, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.userRepository = userRepository;
    }

    private MovieEntity findMovieToWatch(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoWantToWatchId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with given id is not in your to watch list."));
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
        var userEntity = userRepository.findById(userId).orElseThrow();
        var movieEntity =
                movieService.findEntity(dto.movieId());

        try {
            userEntity.getMoviesToWatch().add(movieEntity);
            userRepository.saveAndFlush(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your to watch list.");
        }

        return movieMapper.toResponse(movieEntity);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var movieToWatch = findMovieToWatch(movieId, userId);

        var userEntity = userRepository.findById(userId).orElseThrow();
        userEntity.getMoviesToWatch().remove(movieToWatch);

        userRepository.save(userEntity);
    }
}
