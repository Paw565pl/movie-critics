package dev.paw565pl.movie_critics.movie.data;

import dev.paw565pl.movie_critics.config.TestConfig;
import dev.paw565pl.movie_critics.data.TestDataFactory;
import dev.paw565pl.movie_critics.movie.model.MovieEntity;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;

@TestComponent
@Import({TestConfig.class, GenreTestDataFactory.class})
public class MovieTestDataFactory implements TestDataFactory<MovieEntity> {

    private final Faker faker;
    private final MovieRepository movieRepository;
    private final GenreTestDataFactory genreDataFixture;

    public MovieTestDataFactory(Faker faker, MovieRepository movieRepository, GenreTestDataFactory genreDataFixture) {
        this.faker = faker;
        this.movieRepository = movieRepository;
        this.genreDataFixture = genreDataFixture;
    }

    private MovieEntity generateFakeMovie() {
        var genre = genreDataFixture.createOne();
        var movieEntity = new MovieEntity(faker.book().title(), LocalDate.now(), List.of(genre));

        return movieEntity;
    }

    public MovieEntity createOne() {
        return movieRepository.save(generateFakeMovie());
    }

    public List<MovieEntity> createMany(int count) {
        var movies = new ArrayList<MovieEntity>();
        for (int i = 0; i < count; i++) {
            movies.add(generateFakeMovie());
        }

        return movieRepository.saveAll(movies);
    }

    public void clear() {
        movieRepository.deleteAll();
        genreDataFixture.clear();
    }
}
