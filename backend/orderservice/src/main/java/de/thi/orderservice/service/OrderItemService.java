package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class OrderItemService {

    @Inject
    OrderItemRepository orderItemRepository;

    public Optional<OrderItem> findById(Long productId) {
        return orderItemRepository.findByIdOptional(productId);
    }

    @Transactional
    public OrderItem update(Long id, OrderItem orderItem) {
        OrderItem existingOrderItem = orderItemRepository.findById(id);

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
