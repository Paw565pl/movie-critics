package dev.paw565pl.movie_critics.movie.integration;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.paw565pl.movie_critics.mock.JwtMock;
import dev.paw565pl.movie_critics.movie.dto.MovieDto;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class MovieCreateTest extends MovieTest {

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtDecoder jwtDecoder;

    private String getJsonData() {
        var genre = genreTestDataFactory.createOne();
        var data = new MovieDto(
                "title",
                null,
                LocalDate.now(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(genre.getId()),
                null,
                null,
                null);

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturn401IfUserIsAnonymous() {
        var jsonData = getJsonData();
        given().contentType(ContentType.JSON)
                .body(jsonData)
                .when()
                .post()
                .then()
                .statusCode(401);
    }

    @Test
    void shouldReturn403IfUserIsNotAdmin() {
        var jsonData = getJsonData();

        var mockToken = JwtMock.getToken(List.of());
        when(jwtDecoder.decode(mockToken.getTokenValue())).thenReturn(mockToken);

        given().auth()
                .oauth2(mockToken.getTokenValue())
                .contentType(ContentType.JSON)
                .body(jsonData)
                .when()
                .post()
                .then()
                .statusCode(403);
    }
}
