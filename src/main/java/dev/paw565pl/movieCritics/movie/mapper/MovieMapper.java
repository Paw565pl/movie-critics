package dev.paw565pl.movieCritics.movie.mapper;

import dev.paw565pl.movieCritics.movie.dto.MovieDto;
import dev.paw565pl.movieCritics.movie.model.Movie;
import dev.paw565pl.movieCritics.movie.response.MovieResponse;

public interface MovieMapper {

    Movie toEntity(MovieDto dto);

    MovieResponse toResponseDto(Movie movie);
}
