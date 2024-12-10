package dev.paw565pl.movie_critics.movie.integration;

import dev.paw565pl.movie_critics.IntegrationTest;
import dev.paw565pl.movie_critics.movie.data.GenreTestDataFactory;
import dev.paw565pl.movie_critics.movie.data.MovieTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(MovieTestDataFactory.class)
abstract class MovieTest extends IntegrationTest {

    @Autowired
    MovieTestDataFactory movieTestDataFactory;

    @Autowired
    GenreTestDataFactory genreTestDataFactory;

    @BeforeEach
    void setUp() {
        setBaseUrl("/api/v1/movies");
    }
}
