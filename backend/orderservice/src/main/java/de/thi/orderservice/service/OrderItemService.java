package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderItemRepository;
import de.thi.orderservice.jpa.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class OrderItemService {

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderRepository orderRepository;

    public Optional<OrderItem> findOrderItemById(Long productId) {
        return orderItemRepository.findByIdOptional(productId);
    }

    @Transactional
    public Order updateProductStatus(Long id, Long productId, OrderItem.OrderItemStatus status) {
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder != null) {
            Optional<OrderItem> orderItemOptional = findOrderItemById(productId);
            if (orderItemOptional.isPresent()) {
                OrderItem orderItem = orderItemOptional.get();
                orderItem.setOrderItemStatus(status);
                orderRepository.persist(existingOrder);
                return existingOrder;
            }
        }
        return null;
    }
}
