package dev.paw565pl.movie_critics.movie.specification;

import dev.paw565pl.movie_critics.movie.model.Movie;
import dev.paw565pl.movie_critics.user.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    private MovieSpecification() {}

    public static Specification<Movie> titleContainsIgnoreCase(String title) {
        return (root, query, builder) -> {
            if (title == null) {
                return null;
            }

            return builder.like(
                    builder.lower(root.get("title")),
                    builder.lower(builder.literal("%" + title + "%")));
        };
    }

    public static Specification<Movie> ageRatingEqualsIgnoreCase(String ageRating) {
        return (root, query, builder) -> {
            if (ageRating == null) {
                return null;
            }

            return builder.equal(
                    builder.lower(root.get("ageRating")),
                    builder.lower(builder.literal(ageRating)));
        };
    }

    public static Specification<Movie> releasedAfterOrEquals(LocalDate startReleasedDate) {
        return (root, query, builder) -> {
            if (startReleasedDate == null) {
                return null;
            }

            return builder.greaterThanOrEqualTo(root.get("released"), startReleasedDate);
        };
    }

    public static Specification<Movie> releasedBeforeOrEquals(LocalDate endReleasedDate) {
        return (root, query, builder) -> {
            if (endReleasedDate == null) {
                return null;
            }

            return builder.lessThanOrEqualTo(root.get("released"), endReleasedDate);
        };
    }

    public static Specification<Movie> genreIdsContains(List<Long> genreIds) {
        return (root, query, builder) -> {
            if (genreIds == null || genreIds.isEmpty()) {
                return null;
            }

            return root.get("genres").get("id").in(genreIds);
        };
    }

    public static Specification<Movie> directorIdsContains(List<Long> directorIds) {
        return (root, query, builder) -> {
            if (directorIds == null || directorIds.isEmpty()) {
                return null;
            }

            return root.get("directors").get("id").in(directorIds);
        };
    }

    public static Specification<Movie> writerIdsContains(List<Long> writerIds) {
        return (root, query, builder) -> {
            if (writerIds == null || writerIds.isEmpty()) {
                return null;
            }

            return root.get("writers").get("id").in(writerIds);
        };
    }

    public static Specification<Movie> actorIdsContains(List<Long> actorIds) {
        return (root, query, builder) -> {
            if (actorIds == null || actorIds.isEmpty()) {
                return null;
            }

            return root.get("actors").get("id").in(actorIds);
        };
    }

    public static Specification<Movie> languageContainsIgnoreCase(String language) {
        return (root, query, builder) -> {
            if (language == null) {
                return null;
            }

            return builder.like(
                    builder.lower(root.get("language")),
                    builder.lower(builder.literal("%" + language + "%")));
        };
    }

    public static Specification<Movie> countryContainsIgnoreCase(String country) {
        return (root, query, builder) -> {
            if (country == null) {
                return null;
            }

            return builder.like(
                    builder.lower(root.get("country")),
                    builder.lower(builder.literal("%" + country + "%")));
        };
    }

    public static Specification<Movie> notIgnoredByUser(UUID userId) {
        return (root, query, builder) -> {
            if (userId == null) {
                return null;
            }

            var subquery = query != null ? query.subquery(Long.class) : null;
            if (subquery == null) {
                return null;
            }

            var userRoot = subquery.from(User.class);
            var ignoredMovies = userRoot.join("ignoredMovies");
            subquery.select(ignoredMovies.get("id"))
                    .where(builder.equal(userRoot.get("id"), userId));

            return builder.not(root.get("id").in(subquery));
        };
    }
}
