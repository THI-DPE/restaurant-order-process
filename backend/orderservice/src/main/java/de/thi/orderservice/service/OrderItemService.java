package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderItemRepository;
import de.thi.orderservice.jpa.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class OrderItemService {

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das OrderItemRepository-Objekt wird injiziert.
    @Inject
    OrderItemRepository orderItemRepository;

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das OrderRepository-Objekt wird injiziert.
    @Inject
    OrderRepository orderRepository;

    public Optional<OrderItem> findById(Long productId) {
        return orderItemRepository.findByIdOptional(productId);
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // und Datenbank operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    // Die Methode aktualisiert ProductID, Status und Remark des OrderItems
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
            if (orderItem.getRemark() != null) {
                existingOrderItem.setRemark(orderItem.getRemark());
            }

            orderItemRepository.persist(existingOrderItem);
            return existingOrderItem;
        }
        return null;
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // und Datenbank operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    @Transactional
    public boolean delete(Long orderItemId) {
        return orderItemRepository.deleteById(orderItemId);
    }

}
