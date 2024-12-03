package dev.paw565pl.movie_critics.movie.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class MovieRetrieveTest extends MovieTest {

    @Test
    void shouldReturnNotFound() {
        given().when().get("/{id}", 1).then().statusCode(404);
    }

    @Test
    void shouldReturnMovie() {
        var movie = movieDataFixture.createOne();

        given().when().get("/{id}", movie.getId()).then().statusCode(200).body("title", equalTo(movie.getTitle()));
    }
}
