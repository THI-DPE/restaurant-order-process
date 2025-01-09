package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.entities.ProductCategory;
import de.thi.orderservice.jpa.repository.OrderRepository;
import de.thi.orderservice.rest.dto.CreateOrderDTO;
import de.thi.orderservice.rest.dto.UpdateOrderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.listAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findByIdOptional(id);
    }

    public Order updateOrder(Long id, UpdateOrderDTO updateOrderDTO) {
        Order existingOrder = orderRepository.findById(id);

        if (existingOrder != null) {
            if (updateOrderDTO.getCustomerId() != null) {
                existingOrder.setCustomerId(updateOrderDTO.getCustomerId());
            }
            if (updateOrderDTO.getOrderTimestamp() != null) {
                existingOrder.setOrderTimestamp(updateOrderDTO.getOrderTimestamp());
            }
            if (updateOrderDTO.getProcessorId() != null) {
                existingOrder.setProcessorId(updateOrderDTO.getProcessorId());
            }
            if (updateOrderDTO.getOrderStatus() != null) {
                existingOrder.setStatus(updateOrderDTO.getOrderStatus());
            }

            orderRepository.persist(existingOrder);
            return existingOrder;
        }
        return null;
    }

    @Transactional
    public Order createOrder(CreateOrderDTO orderDTO) {
        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setOrderTimestamp(orderDTO.getOrderTimestamp());
        order.setProcessorId(null);
        order.setStatus(Order.OrderStatus.PROCESSING);

        List<ProductCategory> productCategories = new ArrayList<>();

        for (CreateOrderDTO.ProductCategoryDTO dto : orderDTO.getProducts()) {
            ProductCategory category = new ProductCategory();
            category.setProductCategoryName(dto.getProductCategoryName());

            List<OrderItem> orderItems = new ArrayList<>();
            if (dto.getProductIds() != null) {
                for (Long menuId : dto.getProductIds()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(menuId);
                    orderItem.setOrderItemStatus(OrderItem.OrderItemStatus.PROCESSING);
                    orderItems.add(orderItem);
                }
            }

            category.setOrderItems(orderItems);
            productCategories.add(category);
        }

        order.setProducts(productCategories);

        orderRepository.persist(order);
        return order;
    }


    @Transactional
    public boolean delete(Long id) {
        return orderRepository.deleteById(id);
    }

    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder != null) {
            existingOrder.setStatus(status);
            orderRepository.persist(existingOrder);
            return existingOrder;
        }
        return null;
    }

}