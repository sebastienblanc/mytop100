package io.github.sebastienblanc.mytop100.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class TopSongsControllerTest {

    @Test
    void shouldCreateUserAddSongAndGetTopSongs() {
        given()
                .contentType("application/json")
                .body("{\"userId\":\"api-user\"}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body("{\"track\":\"One More Time\",\"artist\":\"Daft Punk\"}")
                .when()
                .post("/api/users/api-user/top-songs")
                .then()
                .statusCode(201)
                .body("track", equalTo("One More Time"));

        given()
                .when()
                .get("/api/users/api-user/top-songs")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].artist", equalTo("Daft Punk"));
    }
}
