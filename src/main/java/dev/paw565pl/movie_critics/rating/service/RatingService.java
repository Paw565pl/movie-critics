package dev.paw565pl.movie_critics.rating.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.service.MovieService;
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

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final UserRepository userRepository;
    private final MovieService movieService;

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper, UserRepository userRepository, MovieService movieService) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.userRepository = userRepository;
        this.movieService = movieService;
    }


    private RatingEntity findEntity(Long movieId, UUID userId) {
        return ratingRepository
                .findByMovieIdAndAuthorId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "You have not rated this movie."));
    }

    public RatingResponse findByMovieIdAndUserId(Long movieId, Jwt jwt) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var ratingEntity = findEntity(movieId, user.getId());

        return ratingMapper.toResponse(ratingEntity);
    }

    @Transactional
    public RatingResponse create(Long movieId, Jwt jwt, RatingDto dto) {
        var movieEntity = movieService.findEntity(movieId);
        var user = userRepository.findById(UserDetailsImpl.fromJwt(jwt).getId()).orElseThrow();

        var rating = ratingMapper.toEntity(dto);
        rating.setAuthor(user);
        rating.setMovie(movieEntity);

        try {
            var savedRatingEntity = ratingRepository.saveAndFlush(rating);
            return ratingMapper.toResponse(savedRatingEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("You have already rated this movie.");
        }
    }

    @Transactional
    public RatingResponse update(Long movieId, Jwt jwt, RatingDto dto) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var ratingEntity = findEntity(movieId, user.getId());

        ratingEntity.setValue(dto.value());

        var savedRatingEntity = ratingRepository.save(ratingEntity);
        return ratingMapper.toResponse(savedRatingEntity);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        var user = UserDetailsImpl.fromJwt(jwt);
        var ratingEntity = findEntity(movieId, user.getId());

        ratingRepository.deleteById(ratingEntity.getId());
    }
}
