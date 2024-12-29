package dev.paw565pl.movie_critics.favorite_movie.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.favorite_movie.dto.FavoriteMovieDto;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import dev.paw565pl.movie_critics.user.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class FavoriteMovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public FavoriteMovieService(
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

    private MovieEntity findFavoriteMovieEntity(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoFavoritedId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie with given id is not in your favorite list."));
    }

    @Transactional(readOnly = true)
    public Page<MovieResponse> findAll(UserDetailsImpl user, Pageable pageable) {
        var userId = user.getId();

        var movies = movieRepository.findAllByUsersWhoFavoritedId(userId, pageable);
        var movieIds = movies.stream().map(MovieEntity::getId).toList();

        movieRepository.findAllWithGenresByIds(movieIds);
        movieRepository.findAllWithDirectorsByIds(movieIds);
        movieRepository.findAllWithWritersByIds(movieIds);
        movieRepository.findAllWithActorsByIds(movieIds);

        return movies.map(movieMapper::toResponse);
    }

    public MovieResponse findByMovieIdAndUserId(Long movieId, UserDetailsImpl user) {
        var movieEntity = findFavoriteMovieEntity(movieId, user.getId());
        return movieMapper.toResponse(movieEntity);
    }

    @Transactional
    public MovieResponse create(UserDetailsImpl user, FavoriteMovieDto dto) {
        var movieEntity = findMovieEntity(dto.movieId());

        var userId = user.getId();
        var userEntity = userService.findEntityById(userId);

        try {
            userEntity.getFavoriteMovies().add(movieEntity);
            userRepository.saveAndFlush(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your favorite list.");
        }

        return movieMapper.toResponse(movieEntity);
    }

    @Transactional
    public void delete(Long movieId, UserDetailsImpl user) {
        var userId = user.getId();
        var favoriteMovie = findFavoriteMovieEntity(movieId, userId);

        var userEntity = userService.findEntityById(userId);
        userEntity.getFavoriteMovies().remove(favoriteMovie);

        userRepository.save(userEntity);
    }
}
