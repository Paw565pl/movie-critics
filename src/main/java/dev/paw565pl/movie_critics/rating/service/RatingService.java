package dev.paw565pl.movie_critics.rating.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.mapper.RatingMapper;
import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import dev.paw565pl.movie_critics.rating.repository.RatingRepository;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import dev.paw565pl.movie_critics.user.service.UserService;
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
    private final RatingMapper ratingMapper;
    private final UserService userService;
    private final MovieService movieService;

    public RatingService(
            RatingRepository ratingRepository,
            RatingMapper ratingMapper,
            UserService userService,
            MovieService movieService) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.userService = userService;
        this.movieService = movieService;
    }

    private RatingEntity findEntity(Long movieId, UUID userId) {
        return ratingRepository
                .findByMovieIdAndAuthorId(movieId, userId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "You have not rated this movie yet."));
    }

    public RatingResponse findByMovieIdAndUserId(Long movieId, Jwt jwt) {
        movieService.findEntity(movieId);
        var user = UserDetailsImpl.fromJwt(jwt);
        var ratingEntity = findEntity(movieId, user.getId());

        return ratingMapper.toResponse(ratingEntity);
    }

    @Transactional
    public RatingResponse create(Long movieId, Jwt jwt, RatingDto dto) {
        var movieEntity = movieService.findEntity(movieId);

        var userId = UserDetailsImpl.fromJwt(jwt).getId();
        var userEntity = userService.findById(userId);

        var ratingEntity = ratingMapper.toEntity(dto);
        ratingEntity.setAuthor(userEntity);
        ratingEntity.setMovie(movieEntity);

        try {
            var savedRatingEntity = ratingRepository.saveAndFlush(ratingEntity);
            return ratingMapper.toResponse(savedRatingEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("You have already rated this movie.");
        }
    }

    @Transactional
    public RatingResponse update(Long movieId, Jwt jwt, RatingDto dto) {
        movieService.findEntity(movieId);
        var user = UserDetailsImpl.fromJwt(jwt);
        var ratingEntity = findEntity(movieId, user.getId());

        ratingEntity.setValue(dto.value());

        var savedRatingEntity = ratingRepository.save(ratingEntity);
        return ratingMapper.toResponse(savedRatingEntity);
    }

    @Transactional
    public void delete(Long movieId, Jwt jwt) {
        movieService.findEntity(movieId);
        var user = UserDetailsImpl.fromJwt(jwt);
        var ratingEntity = findEntity(movieId, user.getId());

        ratingRepository.deleteById(ratingEntity.getId());
    }
}
