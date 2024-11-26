package dev.paw565pl.movie_critics.rating.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.mapper.RatingMapper;
import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import dev.paw565pl.movie_critics.rating.repository.RatingRepository;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class RatingService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public RatingService(
            UserRepository userRepository,
            MovieRepository movieRepository,
            RatingRepository ratingRepository,
            RatingMapper ratingMapper) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    private RatingEntity findRating(Long movieId, UUID userId) {
        return ratingRepository
                .findByMovieIdAndAuthorId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public RatingResponse findByMovieIdAndUserId(Long movieId, Jwt jwt) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var rating = findRating(movieId, user.getId());

        return ratingMapper.toResponse(rating);
    }

    @Transactional
    public RatingResponse create(Long movieId, Jwt jwt, RatingDto dto) {
        var movie = movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
        var user = userRepository.findById(UserDetailsImpl.fromJwt(jwt).getId()).orElseThrow();

        var rating = ratingMapper.toEntity(dto, movie, user);

        try {
            var savedRating = ratingRepository.saveAndFlush(rating);
            return ratingMapper.toResponse(savedRating);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("You have already rated this movie.");
        }
    }

    @Transactional
    public RatingResponse update(Long movieId, Jwt jwt, RatingDto dto) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var rating = findRating(movieId, user.getId());

        rating.setValue(dto.value());

        var savedRating = ratingRepository.save(rating);
        return ratingMapper.toResponse(savedRating);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var rating = findRating(movieId, user.getId());

        ratingRepository.deleteById(rating.getId());
    }
}
