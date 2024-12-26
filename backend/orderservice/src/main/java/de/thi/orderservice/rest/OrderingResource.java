package de.thi.orderservice.rest;

import de.thi.orderservice.jpa.Ordering;
import de.thi.orderservice.jpa.OrderingRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
}
