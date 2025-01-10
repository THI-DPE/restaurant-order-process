package de.thi.orderservice.jpa.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class OrderItem extends PanacheEntity {

    private Long productId;
    private OrderItemStatus orderItemStatus;

    public enum OrderItemStatus {
        PROCESSING,
        COMPLETED,
        FAILED
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public OrderItemStatus getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(OrderItemStatus status) {
        this.orderItemStatus = status;
    }
}