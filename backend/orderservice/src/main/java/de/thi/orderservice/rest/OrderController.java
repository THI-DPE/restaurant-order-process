package de.thi.orderservice.rest;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.rest.dto.OrderItemPriceDTO;
import de.thi.orderservice.rest.dto.UpdateOrderDTO;
import de.thi.orderservice.rest.dto.UpdateOrderItemStatusDTO;
import de.thi.orderservice.rest.dto.UpdateOrderStatusDTO;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService orderService;

    @Inject
    OrderItemService orderItemService;

    @GET
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(order.get()).build();
    }

    @POST
    public Response createOrder(CreateOrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);
        return Response.status(Response.Status.CREATED).entity(createdOrder).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") Long id, UpdateOrderDTO order) {
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

    @PUT
    @Path("/{id}/status")
    public Response updateOrderStatus(@PathParam("id") Long id, UpdateOrderStatusDTO statusUpdateDTO) {
        Order.OrderStatus status = Order.OrderStatus.valueOf(statusUpdateDTO.getStatus().toUpperCase());
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        if (updatedOrder != null) {
            return Response.ok(updatedOrder).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/orderitem/{orderitemid}")
    public Response updateOrderItemStatus(@PathParam("id") Long id, @PathParam("orderitemid") Long productId, UpdateOrderItemStatusDTO statusUpdateDTO) {
        OrderItem.OrderItemStatus status = OrderItem.OrderItemStatus.valueOf(statusUpdateDTO.getStatus().toUpperCase());
        Order updatedOrder = orderItemService.updateOrderItemStatus(id, productId, status);
        if (updatedOrder == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedOrder).build();
    }

    @GET
    @Path("/{id}/failedItems")
    public Response getFailedItems(@PathParam("id") Long id) {
        List<OrderItemPriceDTO> failedItems = orderItemService.getFailedOrderItems(id);
        return Response.ok(failedItems).build();
    }
}
