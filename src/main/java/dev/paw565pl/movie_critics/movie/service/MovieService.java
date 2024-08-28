package dev.paw565pl.movie_critics.movie.service;

import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import dev.paw565pl.movie_critics.movie.response.MovieResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {

    Page<MovieResponse> findAll(
            Pageable pageable,
            String title,
            String rated,
            LocalDate startReleasedDate,
            LocalDate endReleasedDate,
            List<Long> genresIds,
            List<Long> directorsIds,
            List<Long> writersIds,
            List<Long> actorsIds,
            String language,
            String country);

    Optional<MovieResponse> findById(Long id);

    MovieResponse create(MovieDto dto);
}
