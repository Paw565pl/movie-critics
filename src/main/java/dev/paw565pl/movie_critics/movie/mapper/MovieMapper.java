package dev.paw565pl.movie_critics.movie.mapper;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;

public interface MovieMapper {

    Movie toEntity(MovieDto dto);

    MovieResponse toResponse(Movie movie);
}
