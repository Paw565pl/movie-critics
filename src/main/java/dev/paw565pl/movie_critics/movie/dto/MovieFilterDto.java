package dev.paw565pl.movie_critics.movie.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieFilterDto(
        String title,
        String rated,
        LocalDate startReleasedDate,
        LocalDate endReleasedDate,
        List<Long> genreIds,
        List<Long> directorIds,
        List<Long> writerIds,
        List<Long> actorIds,
        String language,
        String country) {}
