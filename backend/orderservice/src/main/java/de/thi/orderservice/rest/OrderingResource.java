package de.thi.orderservice.rest;

import de.thi.orderservice.jpa.Ordering;
import de.thi.orderservice.jpa.OrderingRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/orders")
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
}
