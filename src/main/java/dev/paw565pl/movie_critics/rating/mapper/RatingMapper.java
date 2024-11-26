package dev.paw565pl.movie_critics.rating.mapper;

import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.model.Rating;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import dev.paw565pl.movie_critics.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public Rating toEntity(RatingDto dto, MovieEntity movieEntity, User user) {
        var rating = new Rating(dto.value(), movieEntity);
        rating.setAuthor(user);

        return rating;
    }

    public RatingResponse toResponse(Rating rating) {
        return new RatingResponse(rating.getValue());
    }
}
