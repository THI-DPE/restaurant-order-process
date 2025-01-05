package de.thi.orderservice.rest;

import de.thi.orderservice.jpa.Drink;
import de.thi.orderservice.jpa.Meal;
import de.thi.orderservice.jpa.Ordering;
import de.thi.orderservice.jpa.OrderingRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;

import java.util.List;

@Path("/orders") //class level path
public class OrderingResource {

    private final OrderingRepository orderingRepository;

    public OrderingResource(OrderingRepository orderingRepository) {
        this.orderingRepository = orderingRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ordering> getOrders() {
        return orderingRepository.listAll();
    }

    // GET endpoint to search for orders by ID
    @GET
    @Path("/{id}") // Method level path
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") Long id) { // @PathParam binds URL path id to method parameter id
        Ordering ordering = orderingRepository.findById(id);
        if(ordering == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); //404 error handling
        }
        return Response.ok(ordering).build(); // 200
    }

    // POST endpoint to create a new order
    @POST
    @Consumes(MediaType.APPLICATION_JSON) // Explicitly specify JSON as input media type for clarity (JSON would be Quarkus default)
    @Produces(MediaType.APPLICATION_JSON) // Explicitly specify JSON output media type for clarity (JSON would be Quarkus default)
    @Transactional // Transactional annotation ensures that the method runs within a transaction
    public Response createOrder(Ordering ordering) {
        ordering.persist();
        return Response.status(Response.Status.CREATED).entity(ordering).build();
        // Response --> A JAX class for building and returning HTTP responses
        // status(Response.Status.CREATED) --> HTTP status code 201 for created
        // entity(ordering) --> adds newly created ordering object as response payload
        // build(): constructs response object
    }

    // PUT endpoint to update an order
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateOrder(@PathParam("id") Long id, Ordering ordering){
        Ordering existingOrdering = orderingRepository.findById(id);
        if (existingOrdering == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity("Order " + id + " not found").build()); //404 error handling
        }

        existingOrdering.setCustomerId(ordering.getCustomerId());
        existingOrdering.setDrinks(ordering.getDrinks());
        existingOrdering.setMeals(ordering.getMeals());
        existingOrdering.setStatus(ordering.getStatus());
        existingOrdering.setTimestamp(ordering.getTimestamp());
        existingOrdering.setProcessorId(ordering.getProcessorId());

        orderingRepository.persist(existingOrdering);

        return Response.ok(existingOrdering).build();
    }


    // DELETE endpoint to delete an order
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteOrder(@PathParam("id") Long id){
        Ordering ordering = orderingRepository.findById(id);
        if (ordering == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity("Order " + id + " not found").build()); //404 error handling
        }
        orderingRepository.delete(ordering);
        return Response.noContent().build(); // 204 No Content
    }

    @PUT
    @Path("/{id}/meals/{mealId}")
    @Transactional
    public Response removeMeal(@PathParam("id") long id, @PathParam("mealId") long mealId) {
        Ordering ordering = orderingRepository.findById(id);
        if (ordering == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity("Order " + id + " not found").build()); //404 error handling
        }

        double price = 0.0;

        List<Meal> meals = ordering.getMeals();
        for (Meal meal : meals){
            if (meal.getId() == mealId){
                price = meal.getPrice();
                meals.remove(meal);
                break;
            }
        }

        ordering.setMeals(meals);
        orderingRepository.persist(ordering);

        return Response.ok(price).build();
    }

    @PUT
    @Path("/{id}/drinks/{drinkId}")
    @Transactional
    public Response removeDrink(@PathParam("id") long id, @PathParam("drinkId") long drinkId) {
        Ordering ordering = orderingRepository.findById(id);
        if (ordering == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity("Order " + id + " not found").build()); //404 error handling
        }

        double price = 0.0;

        List<Drink> drinks = ordering.getDrinks();
        for (Drink drink : drinks){
            if (drink.getId() == drinkId){
                price = drink.getPrice();
                drinks.remove(drink);
                break;
            }
        }

        ordering.setDrinks(drinks);
        orderingRepository.persist(ordering);

        return Response.ok(price).build();
    }

}
