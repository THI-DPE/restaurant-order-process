package de.thi.orderservice;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class OrderingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/orders")
          .then()
             .statusCode(200);
    }

}