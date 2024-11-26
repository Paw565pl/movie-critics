package dev.paw565pl.movie_critics.favorite_movie.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.favorite_movie.dto.FavoriteMovieDto;
import dev.paw565pl.movie_critics.movie.mapper.MovieMapper;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
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
public class FavoriteMovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieService movieService;
    private final UserRepository userRepository;

    public FavoriteMovieService(MovieRepository movieRepository, MovieMapper movieMapper, MovieService movieService, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.movieService = movieService;
        this.userRepository = userRepository;
    }

    private MovieEntity findFavoriteMovie(Long movieId, UUID userId) {
        return movieRepository
                .findByIdAndUsersWhoFavoritedId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with given id is not in your favorite list."));
    }

    public Page<MovieResponse> findAll(Jwt jwt, Pageable pageable) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        return movieRepository
                .findAllByUsersWhoFavoritedId(userId, pageable)
                .map(movieMapper::toResponse);
    }

    @Transactional
    public MovieResponse create(Jwt jwt, FavoriteMovieDto dto) {
        var movieEntity = movieService.findEntity(dto.movieId());

        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var userEntity = userRepository.findById(userId).orElseThrow();

        try {
            userEntity.getFavoriteMovies().add(movieEntity);
            userRepository.saveAndFlush(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Movie is already in your favorite list.");
        }

        return movieMapper.toResponse(movieEntity);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var favoriteMovie = findFavoriteMovie(movieId, userId);

        var userEntity = userRepository.findById(userId).orElseThrow();
        userEntity.getFavoriteMovies().remove(favoriteMovie);

        userRepository.save(userEntity);
    }
}
