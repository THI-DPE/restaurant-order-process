package de.thi.orderservice.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private LocalDateTime orderTimestamp;

    @OneToMany(cascade = CascadeType.ALL) // CascadeType.ALL: If the ProductCategory entity is removed, all associated OrderItem entities should be removed as well. + Orphan removal: If an OrderItem entity is removed from the ProductCategory entity, it should be removed from the database as well.
    private List<ProductCategory> products;

    private Long processorId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public enum OrderStatus {
        PROCESSING,
        COMPLETED,
        FAILED
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(LocalDateTime orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public List<ProductCategory> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCategory> products) {
        this.products = products;
    }

    public Long getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Long processorId) {
        this.processorId = processorId;
    }

    public OrderStatus getStatus() {
        return orderStatus;
    }

    public void setStatus(OrderStatus status) {
        this.orderStatus = status;
    }
}
