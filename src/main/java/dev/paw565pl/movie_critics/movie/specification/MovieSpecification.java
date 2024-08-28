package dev.paw565pl.movie_critics.movie.specification;

import dev.paw565pl.movie_critics.movie.model.Movie;
import java.time.LocalDate;
import java.util.List;
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

    public static Specification<Movie> ratedEqualsIgnoreCase(String rated) {
        return (root, query, builder) -> {
            if (rated == null) {
                return null;
            }

            return builder.equal(
                    builder.lower(root.get("rated")), builder.lower(builder.literal(rated)));
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

    public static Specification<Movie> genresIdsContains(List<Long> genresIds) {
        return (root, query, builder) -> {
            if (genresIds == null || genresIds.isEmpty()) {
                return null;
            }

            return root.get("genres").get("id").in(genresIds);
        };
    }
}
