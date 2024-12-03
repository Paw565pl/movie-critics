package dev.paw565pl.movie_critics.movie.data;

import dev.paw565pl.movie_critics.config.TestConfig;
import dev.paw565pl.movie_critics.data.TestDataFactory;
import dev.paw565pl.movie_critics.movie.model.GenreEntity;
import dev.paw565pl.movie_critics.movie.repository.GenreRepository;
import java.util.ArrayList;
import java.util.List;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;

@TestComponent
@Import(TestConfig.class)
public class GenreTestDataFactory implements TestDataFactory<GenreEntity> {

    private final Faker faker;
    private final GenreRepository genreRepository;

    public GenreTestDataFactory(Faker faker, GenreRepository genreRepository) {
        this.faker = faker;
        this.genreRepository = genreRepository;
    }

    private GenreEntity generateFakeGenre() {
        return new GenreEntity(faker.funnyName().name());
    }

    public GenreEntity createOne() {
        return genreRepository.save(generateFakeGenre());
    }

    public List<GenreEntity> createMany(int count) {
        var genres = new ArrayList<GenreEntity>();
        for (int i = 0; i < count; i++) {
            genres.add(generateFakeGenre());
        }

        return genreRepository.saveAll(genres);
    }

    public void clear() {
        genreRepository.deleteAll();
    }
}
