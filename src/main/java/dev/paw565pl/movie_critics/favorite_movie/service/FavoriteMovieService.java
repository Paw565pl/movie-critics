package dev.paw565pl.movie_critics.favorite_movie.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.favorite_movie.dto.FavoriteMovieDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
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
public class FavoriteMovieService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public FavoriteMovieService(
            UserRepository userRepository,
            MovieRepository movieRepository,
            MovieMapper movieMapper) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    private MovieEntity findFavoriteMovie(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoFavoritedId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Page<MovieResponse> findAll(Jwt jwt, Pageable pageable) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        return movieRepository
                .findAllByUsersWhoFavoritedId(userId, pageable)
                .map(movieMapper::toResponse);
    }

    @Transactional
    public MovieResponse create(Jwt jwt, FavoriteMovieDto dto) {
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
            user.getFavoriteMovies().add(movie);
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your favorite list.");
        }

        return movieMapper.toResponse(movie);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var favoriteMovie = findFavoriteMovie(movieId, userId);

        var user = userRepository.findById(userId).orElseThrow();
        user.getFavoriteMovies().remove(favoriteMovie);
        userRepository.save(user);
    }
}
