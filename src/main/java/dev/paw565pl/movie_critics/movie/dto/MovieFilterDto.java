package dev.paw565pl.movie_critics.movie.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieFilterDto(
        String title,
        String rated,
        LocalDate startReleasedDate,
        LocalDate endReleasedDate,
        List<Long> genresIds,
        List<Long> directorsIds,
        List<Long> writersIds,
        List<Long> actorsIds,
        String language,
        String country) {}
