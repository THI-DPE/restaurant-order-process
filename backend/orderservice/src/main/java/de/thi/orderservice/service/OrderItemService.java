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

    public Optional<OrderItem> findById(Long productId) {
        return orderItemRepository.findByIdOptional(productId);
    }

    @Transactional
    public OrderItem update(Long orderId, Long orderItemId,OrderItem orderItem) {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
            return null;
        }

        OrderItem existingOrderItem = orderItemRepository.findById(orderItemId);

        if (existingOrderItem != null) {
            if (orderItem.getProductId() != null) {
                existingOrderItem.setProductId(orderItem.getProductId());
            }
            if (orderItem.getStatus() != null) {
                existingOrderItem.setStatus(orderItem.getStatus());
            }

            orderItemRepository.persist(existingOrderItem);
            return existingOrderItem;
        }
        return null;
    }

    @Transactional
    public boolean delete(Long orderItemId) {
        return orderItemRepository.deleteById(orderItemId);
    }

}
