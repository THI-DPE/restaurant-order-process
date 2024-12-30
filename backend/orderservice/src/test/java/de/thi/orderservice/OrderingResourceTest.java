package de.thi.orderservice;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;


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

    // Test for GET method to fetch all orders (Author: Ralf)
    @Test // JUnit annotation
    public void testGetOrders() {
        // usage of REST assured Java library for testing REST APIs
        given() // sets up request (query parameters, headers or body can be added)
                .when().get("/orders") // when() signals start of the actual request
                .then() // then() signals response validation
                .statusCode(200)
                .contentType(ContentType.JSON);
                //.body("$.size()", is(0)); // Assuming there are no orders initially
    }

    // Test for POST method to create a new order (Author: Ralf)
    @Test
    public void testCreateOrder() {
        String orderJson = "{ \"customerId\": 1, \"meals\": [1], \"drinks\": [1], \"timestamp\": \"2024-12-26T12:00:00\", \"processorId\": 1, \"status\": \"PROCESSING\" }";

        given()
                .contentType(ContentType.JSON)
                .body(orderJson)
                .when().post("/orders")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("customerId", is(1))
                .body("status", is("PROCESSING"));
    }

    // Test for GET orderById (Author: Ralf)
    @Test
    @Transactional // Ensures that data setup and cleanup occur within a transaction
    public void testGetOrderById() {
        // Create an order for testing
        String orderJson = "{ \"customerId\": 1, \"meals\": [1], \"drinks\": [1], \"timestamp\": \"2024-12-26T12:00:00\", \"processorId\": 1, \"status\": \"PROCESSING\" }";

        // POST request to create the order and extract the generated order ID
        Long orderId = given()
                .contentType(ContentType.JSON)
                .body(orderJson)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .path("id"); // Extract the generated order ID from the response

        // Step 2: Retrieve the order by its ID and validate the response
        given()
                .pathParam("id", orderId) // Set the path parameter for the order ID
                .when()
                .get("/orders/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", is(orderId.intValue())) // Check that the ID matches
                // intValue --> convert Long value of orderId to int (orderId from Ordering entity is inherited from PanacheEntity and the id field there is a long while response will be int
                // is() --> static method from Hamcrest library that creates a matcher to compare the actual value in the response with the expected value
                .body("customerId", is(1)) // Check that the customer ID is correct
                .body("status", is("PROCESSING")); // Validate the status
    }
}