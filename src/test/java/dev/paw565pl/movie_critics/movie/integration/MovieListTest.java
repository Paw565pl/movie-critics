package dev.paw565pl.movie_critics.movie.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;

class MovieListTest extends MovieTest {

    @Test
    void shouldReturnEmptyList() {
        given().when().get().then().statusCode(200).body("page.totalElements", equalTo(0));
    }

    @Test
    void shouldReturnAllMovies() {
        var moviesCount = movieDataFixture.createMany(3).size();
        given().when().get().then().statusCode(200).body("page.totalElements", equalTo(moviesCount));
    }

    @Test
    void shouldReturnFilteredMovies() {
        var movies = movieDataFixture.createMany(3);
        var title = movies.getFirst().getTitle();

        given().param("title", title)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("content", hasSize(1), "content[0].title", equalTo(title));
    }

    @Test
    void shouldUseSizeParameter() {
        var moviesCount = movieDataFixture.createMany(2).size();
        var pageSize = 1;

        given().param("size", pageSize)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("content", hasSize(pageSize), "page.totalElements", equalTo(moviesCount));
    }
}
