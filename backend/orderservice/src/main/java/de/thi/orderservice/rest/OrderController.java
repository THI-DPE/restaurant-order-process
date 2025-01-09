package de.thi.orderservice.rest;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.rest.dto.UpdateOrderItemStatusDTO;
import de.thi.orderservice.rest.dto.UpdateOrderStatusDTO;
import de.thi.orderservice.service.OrderItemService;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.entities.ProductCategory;
import de.thi.orderservice.rest.dto.CreateOrderDTO;
import de.thi.orderservice.service.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setOrderTimestamp(orderDTO.getOrderTimestamp());
        order.setProcessorId(null);
        order.setStatus(Order.OrderStatus.PROCESSING);

        List<ProductCategory> productCategories = new ArrayList<>();

        for (CreateOrderDTO.ProductCategoryDTO dto : orderDTO.getProducts()) {
            ProductCategory category = new ProductCategory();
            // TODO: Hier könnte man noch einen Check machen, ob es die Category gibt im MenuService
            category.setProductCategoryName(dto.getProductCategoryName());

            List<OrderItem> orderItems = new ArrayList<>();
            for (Long menuId : dto.getProductIds()) {
                OrderItem orderItem = new OrderItem();
                // TODO: Rename zu ProductId
                orderItem.setMenuId(menuId);
                orderItem.setOrderItemStatus(OrderItem.OrderItemStatus.PROCESSING);
                orderItems.add(orderItem);
            }

            category.setOrderItems(orderItems);
            productCategories.add(category);
        }

        order.setProducts(productCategories);

        Order createdOrder = orderService.create(order);
        return Response.status(Response.Status.CREATED).entity(createdOrder).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") Long id, Order order) {
        Order updatedOrder = orderService.update(id, order);
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
    @Path("/{id}/productId/{productId}")
    public Response updateProductStatus(@PathParam("id") Long id, @PathParam("productId") Long productId, UpdateOrderItemStatusDTO statusUpdateDTO) {
        OrderItem.OrderItemStatus status = OrderItem.OrderItemStatus.valueOf(statusUpdateDTO.getStatus().toUpperCase());
        Order updatedOrder = orderItemService.updateProductStatus(id, productId, status);
        return Response.ok(updatedOrder).build();
    }
}
