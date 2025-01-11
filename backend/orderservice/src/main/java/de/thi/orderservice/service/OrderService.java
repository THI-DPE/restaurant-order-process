package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderRepository;
import de.thi.orderservice.rest.dto.CreateOrderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

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

    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id);

        if (existingOrder != null) {
            if(order.getCustomerId() != null) {
                existingOrder.setCustomerId(order.getCustomerId());
            }
            if(order.getProcessorId() != null) {
                existingOrder.setProcessorId(order.getProcessorId());
            }
            if(order.getOrderTimestamp() != null) {
                existingOrder.setOrderTimestamp(order.getOrderTimestamp());
            }
            if(order.getStatus() != null) {
                existingOrder.setStatus(order.getStatus());
            }
            if(order.getOrderItems() != null) {
                existingOrder.setOrderItems(order.getOrderItems());
            }
            if(order.getPaymentDetails() != null) {
                existingOrder.setPaymentDetails(order.getPaymentDetails());
            }
            if(order.getPaymentType() != null) {
                existingOrder.setPaymentType(order.getPaymentType());
            }
            existingOrder.updateCategories();
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

        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderDTO.ProductCategoryDTO dto : orderDTO.getProducts()) {
            if (dto.getProductIds() != null) {
                for (Long productId : dto.getProductIds()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(productId);
                    orderItem.setCategory(dto.getProductCategoryName());
                    orderItem.setStatus(OrderItem.OrderItemStatus.PROCESSING);
                    orderItem.setOrder(order);
                    orderItems.add(orderItem);
                }
            }

        }

        order.setOrderItems(orderItems);
        order.updateCategories();

        orderRepository.persist(order);
        return order;
    }

    @Transactional
    public boolean delete(Long id) {
        return orderRepository.deleteById(id);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long id, String category, String status) {
        Order order = orderRepository.findById(id);
        if (order != null) {
            List<OrderItem> orderItems = order.getOrderItems();
            if (category != null) {
                orderItems.removeIf(orderItem -> !orderItem.getCategory().equals(category));
            }
            if (status != null) {
                orderItems.removeIf(orderItem -> !orderItem.getStatus().equals(OrderItem.OrderItemStatus.valueOf(status)));
            }
            return orderItems;
        }
       return null;
    }
}