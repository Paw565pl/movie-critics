package dev.paw565pl.movie_critics.movie_to_ignore.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.movie_to_ignore.dto.MovieToIgnoreDto;
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
public class MovieToIgnoreService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieService movieService;
    private final UserRepository userRepository;

    public MovieToIgnoreService(MovieRepository movieRepository, MovieMapper movieMapper, MovieService movieService, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.movieService = movieService;
        this.userRepository = userRepository;
    }

    private MovieEntity findMovieToIgnore(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoIgnoredId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with given id is not in your ignored list."));
    }

    public Page<MovieResponse> findAll(Jwt jwt, Pageable pageable) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        return movieRepository
                .findAllByUsersWhoIgnoredId(userId, pageable)
                .map(movieMapper::toResponse);
    }

    @Transactional
    public MovieResponse create(Jwt jwt, MovieToIgnoreDto dto) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var userEntity = userRepository.findById(userId).orElseThrow();
        var movie = movieService.findEntity(dto.movieId());

        try {
            userEntity.getIgnoredMovies().add(movie);
            userRepository.saveAndFlush(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your ignored list.");
        }

        return movieMapper.toResponse(movie);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var movieToIgnore = findMovieToIgnore(movieId, userId);

        var userEntity = userRepository.findById(userId).orElseThrow();
        userEntity.getIgnoredMovies().remove(movieToIgnore);

        userRepository.save(userEntity);
    }
}
