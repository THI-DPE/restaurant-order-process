package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderRepository;
import de.thi.orderservice.rest.dto.CreateOrderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class OrderService {

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das OrderRepository-Objekt wird injiziert.
    @Inject
    OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.listAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findByIdOptional(id);
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // und Datenbank operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    // Die Methode aktualisiert die Bestellung mit allen Attributen
    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id);

        if (existingOrder != null) {
            if(order.getCustomerId() != null) {
                existingOrder.setCustomerId(order.getCustomerId());
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

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // und Datenbank operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    // Die Methode erstellt eine Bestellung
    @Transactional
    public Order createOrder(CreateOrderDTO orderDTO) {
        // Order anlegen
        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setOrderTimestamp(orderDTO.getOrderTimestamp());
        order.setStatus(Order.OrderStatus.PROCESSING);
        order.setPaymentType(Order.PaymentType.valueOf(orderDTO.getPaymentType()));
        order.setPaymentDetails(orderDTO.getPaymentDetails());

        List<OrderItem> orderItems = new ArrayList<>();
        // Iteriert über die Produkte im DTO und fügt sie der Bestellung hinzu
        for (CreateOrderDTO.ProductCategoryDTO dto : orderDTO.getProducts()) {
            if (dto.getOrderItems() != null) {
                // Iteriert über die OrderItems im DTO und fügt sie der Bestellung hinzu
                for (CreateOrderDTO.ProductCategoryDTO.OrderItemDTO itemDTO : dto.getOrderItems()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemDTO.getProductId());
                    orderItem.setCategory(dto.getProductCategoryName());
                    orderItem.setStatus(OrderItem.OrderItemStatus.PROCESSING);
                    orderItem.setOrder(order);
                    orderItem.setRemark(itemDTO.getRemark());
                    orderItems.add(orderItem);
                }
            }
        }
        // Setzt die OrderItems in der Bestellung
        order.setOrderItems(orderItems);
        // Kategorien der Order aktualisieren
        order.updateCategories();

        orderRepository.persist(order);
        return order;
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // und Datenbank operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    @Transactional
    public boolean delete(Long id) {
        return orderRepository.deleteById(id);
    }

    // Gibt eine Liste von OrderItems zurück, die zu einer Bestellung gehören
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
       return new ArrayList<>();
    }
}