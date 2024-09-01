package dev.paw565pl.movie_critics.rating.service;

import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.model.Rating;
import dev.paw565pl.movie_critics.rating.repository.RatingRepository;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;

    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
    }

    private Movie findMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    private Rating findRating(Long movieId, UUID userId) {
        findMovieById(movieId);
        return ratingRepository
                .findByMovieIdAndUserId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public RatingResponse findByMovieIdAndUserId(Long movieId, UUID userId) {
        var rating = findRating(movieId, userId);
        return new RatingResponse(rating.getValue());
    }

    @Transactional
    public RatingResponse create(Long movieId, UUID userId, RatingDto dto) {
        var movie = findMovieById(movieId);
        var rating = new Rating(dto.value(), userId, movie);

        try {
            var savedRating = ratingRepository.saveAndFlush(rating);
            return new RatingResponse(savedRating.getValue());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("You have already rated this movie.");
        }
    }

    @Transactional
    public RatingResponse update(Long movieId, UUID userId, RatingDto dto) {
        var rating = findRating(movieId, userId);
        rating.setValue(dto.value());

        var savedRating = ratingRepository.save(rating);
        return new RatingResponse(savedRating.getValue());
    }

    @Transactional
    public void delete(Long movieId, UUID userId) {
        var rating = findRating(movieId, userId);
        ratingRepository.deleteById(rating.getId());
    }
}
