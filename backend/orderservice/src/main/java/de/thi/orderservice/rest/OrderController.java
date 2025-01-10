package de.thi.orderservice.rest;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.service.OrderItemService;
import de.thi.orderservice.rest.dto.CreateOrderDTO;
import de.thi.orderservice.service.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/orders")
public class OrderController {

    @Inject
    OrderService orderService;

    @Inject
    OrderItemService orderItemService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(CreateOrderDTO createOrderDTO) {
        Order createdOrder = orderService.createOrder(createOrderDTO);
        return Response.status(Response.Status.CREATED).entity(createdOrder).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") Long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(order.get()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") Long id, Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        if (updatedOrder != null) {
            return Response.ok(updatedOrder).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long id) {
        boolean deleted = orderService.delete(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderItemsByOrderId(@PathParam("id") Long id, @QueryParam("status") String status) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(id, status);
        return Response.ok(orderItems).build();
    }

    @GET
    @Path("/{orderId}/items/{orderItemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderItemById(@PathParam("orderItemId") Long orderItemId) {
        Optional<OrderItem> orderItem = orderItemService.findById(orderItemId);
        if (orderItem.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(orderItem.get()).build();
    }

    @PUT
    @Path("/{orderId}/items/{orderItemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOrderItem(@PathParam("orderItemId") Long orderItemId, OrderItem orderItem) {
        OrderItem updatedOrderItem = orderItemService.update(orderItemId, orderItem);
        if (updatedOrderItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedOrderItem).build();
    }

    @DELETE
    @Path("/{orderId}/items/{orderItemId}")
    public Response deleteOrderItem(@PathParam("orderItemId") Long orderItemId) {
        boolean deleted = orderItemService.delete(orderItemId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
