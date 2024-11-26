package dev.paw565pl.movie_critics.rating.mapper;

import dev.paw565pl.movie_critics.rating.dto.RatingDto;
import dev.paw565pl.movie_critics.rating.model.RatingEntity;
import dev.paw565pl.movie_critics.rating.response.RatingResponse;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public RatingEntity toEntity(RatingDto dto) {
        var rating = new RatingEntity();
        rating.setValue(dto.value());

        return rating;
    }

    public RatingResponse toResponse(RatingEntity ratingEntity) {
        return new RatingResponse(ratingEntity.getValue());
    }
}
