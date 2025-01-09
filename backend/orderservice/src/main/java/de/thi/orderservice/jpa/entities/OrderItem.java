package de.thi.orderservice.jpa.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class OrderItem extends PanacheEntity {

    private Long menuId;
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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public OrderItemStatus getOrderItemstatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(OrderItemStatus status) {
        this.orderItemStatus = status;
    }
}