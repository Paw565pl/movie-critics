package dev.paw565pl.movie_critics.rating.service;

import dev.paw565pl.movie_critics.auth.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.model.Rating;
import dev.paw565pl.movie_critics.rating.repository.RatingRepository;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;

    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
    }

    private Rating findRating(Long movieId, UUID userId) {
        return ratingRepository
                .findByMovieIdAndUserId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public RatingResponse findByMovieIdAndUserId(Long movieId, Jwt jwt) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var rating = findRating(movieId, user.getId());

        return new RatingResponse(rating.getValue());
    }

    @Transactional
    public RatingResponse create(Long movieId, Jwt jwt, RatingDto dto) {
        var movie = movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
        var user = UserDetailsImpl.fromJwt(jwt);

        var rating = new Rating(dto.value(), user.getUsername(), user.getId(), movie);

        try {
            var savedRating = ratingRepository.saveAndFlush(rating);
            return new RatingResponse(savedRating.getValue());
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
        return new RatingResponse(savedRating.getValue());
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var rating = findRating(movieId, user.getId());

        ratingRepository.deleteById(rating.getId());
    }
}
