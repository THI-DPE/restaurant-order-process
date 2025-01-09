package de.thi.orderservice;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


import static io.restassured.RestAssured.given;

@QuarkusTest
class CategoryResourceTest {

    // Test for GET method to fetch all orders (Author: Ralf)
    @Test // JUnit annotation
    void testGetOrders() {
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
    void testCreateOrder() {
        // Create an order for testing
        Ordering ordering = new Ordering();
        ordering.setCustomerId(1L);
        ordering.setStatus(Ordering.StatusEnum.PROCESSING);
        ordering.setTimestamp(LocalDateTime.parse("2024-12-26T12:00:00"));
        ordering.setProcessorId(1L);
        ordering.setMeals(List.of(new de.thi.orderservice.jpa.Product()));
        ordering.setDrinks(List.of(new de.thi.orderservice.jpa.Category()));


        given()
                .contentType(ContentType.JSON)
                .body(ordering)
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
    void testGetOrderById() {
        // Create an order for testing
        Ordering ordering = new Ordering();
        ordering.setCustomerId(1L);
        ordering.setStatus(Ordering.StatusEnum.PROCESSING);
        ordering.setTimestamp(LocalDateTime.parse("2024-12-26T12:00:00"));
        ordering.setProcessorId(1L);
        ordering.setMeals(List.of(new de.thi.orderservice.jpa.Product()));
        ordering.setDrinks(List.of(new de.thi.orderservice.jpa.Category()));

        // POST request to create the order and extract the generated order ID
        Long orderId = given()
                .contentType(ContentType.JSON)
                .body(ordering)
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
                .body("id", is(orderId))
                .body("customerId", is(1)) // Check that the customer ID is correct
                .body("status", is("PROCESSING")); // Validate the status
    }

    @Test
    void testUpdateOrder(){
        Ordering ordering = new Ordering();
        ordering.setCustomerId(1L);
        ordering.setDrinks(List.of(new de.thi.orderservice.jpa.Category(), new de.thi.orderservice.jpa.Category()));
        ordering.setMeals(List.of(new de.thi.orderservice.jpa.Product(), new de.thi.orderservice.jpa.Product()));
        ordering.setStatus(Ordering.StatusEnum.PROCESSING);
        ordering.setTimestamp(LocalDateTime.parse("2025-01-03T12:00:00"));
        ordering.setProcessorId(1L);

        // create order
        int id = given()
                .contentType(ContentType.JSON)
                .body(ordering)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().path("id");

        Ordering updatedOrdering = new Ordering();
        updatedOrdering.setCustomerId(2L);
        updatedOrdering.setDrinks(List.of(new de.thi.orderservice.jpa.Category()));
        updatedOrdering.setMeals(List.of(new de.thi.orderservice.jpa.Product()));
        updatedOrdering.setStatus(Ordering.StatusEnum.COMPLETED);
        updatedOrdering.setTimestamp(LocalDateTime.parse("2025-01-03T13:00:00"));
        updatedOrdering.setProcessorId(2L);

        // update order
        given()
                .contentType(ContentType.JSON)
                .body(updatedOrdering)
                .when()
                .put("/orders/" + id)
                .then()
                .statusCode(200)
                .body("customerId", is(2))
                .body("drinks", hasSize(1))
                .body("meals", hasSize(1))
                .body("status", is("COMPLETED"))
                .body("timestamp", is("2025-01-03T13:00:00"))
                .body("processorId", is(2));

    }

    @Test
    void testDeleteOrder() {
        Ordering ordering = new Ordering();
        ordering.setCustomerId(1L);
        ordering.setDrinks(List.of(new de.thi.orderservice.jpa.Category()));
        ordering.setMeals(List.of(new de.thi.orderservice.jpa.Product()));
        ordering.setStatus(Ordering.StatusEnum.PROCESSING);
        ordering.setTimestamp(LocalDateTime.parse("2025-01-03T12:00:00"));
        ordering.setProcessorId(1L);

        // create order
        int id = given()
                .contentType(ContentType.JSON)
                .body(ordering)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().path("id");

        // delete order
        given()
                .when()
                .delete("/orders/" + id)
                .then()
                .statusCode(204);

        // Check if order has been deleted
        given()
                .when()
                .get("/orders/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void testRemoveDrink(){
        de.thi.orderservice.jpa.Category category = new de.thi.orderservice.jpa.Category();
        category.setPrice(1D);

        Ordering ordering = new Ordering();
        ordering.setCustomerId(1L);
        ordering.setDrinks(List.of(category));
        ordering.setMeals(List.of(new de.thi.orderservice.jpa.Product()));
        ordering.setStatus(Ordering.StatusEnum.PROCESSING);
        ordering.setTimestamp(LocalDateTime.parse("2025-01-05T12:00:00"));
        ordering.setProcessorId(1L);

        // create order
        int id = given()
                .contentType(ContentType.JSON)
                .body(ordering)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().path("id");

        // get id of drink in created order
        int drinkId = given()
                .when()
                .get("/orders/" + id)
                .then()
                .statusCode(200)
                .extract().path("drinks[0].id");

        // remove drink
        given()
                .when()
                .put("/orders/" + id + "/drinks/" + drinkId)
                .then()
                .statusCode(200)
                .body(is("1.0"));

    }

    @Test
    void testRemoveMeal(){
        de.thi.orderservice.jpa.Product product = new de.thi.orderservice.jpa.Product();
        product.setPrice(1D);

        Ordering ordering = new Ordering();
        ordering.setCustomerId(1L);
        ordering.setDrinks(List.of(new de.thi.orderservice.jpa.Category()));
        ordering.setMeals(List.of(product));
        ordering.setStatus(Ordering.StatusEnum.PROCESSING);
        ordering.setTimestamp(LocalDateTime.parse("2025-01-05T12:00:00"));
        ordering.setProcessorId(1L);

        // create order
        int id = given()
                .contentType(ContentType.JSON)
                .body(ordering)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().path("id");

        // get id of meal in created order
        int mealId = given()
                .when()
                .get("/orders/" + id)
                .then()
                .statusCode(200)
                .extract().path("meals[0].id");

        // remove meal
        given()
                .when()
                .put("/orders/" + id + "/meals/" + mealId)
                .then()
                .statusCode(200)
                .body(is("1.0"));

    }

}