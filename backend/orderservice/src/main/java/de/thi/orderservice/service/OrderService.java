package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    public Order create(Order order) {

        order.setStatus(Order.OrderStatus.PROCESSING);
        orderRepository.persist(order);
        return order;
    }

    @Transactional
    public Order update(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder != null) {
            existingOrder.setCustomerId(order.getCustomerId());
            existingOrder.setOrderTimestamp(order.getOrderTimestamp());
            existingOrder.setProcessorId(order.getProcessorId());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setProducts(existingOrder.getProducts());

            orderRepository.persist(existingOrder);
            return existingOrder;
        }
        return null;
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