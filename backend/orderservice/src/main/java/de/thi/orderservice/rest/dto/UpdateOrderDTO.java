package de.thi.orderservice.rest.dto;

import de.thi.orderservice.jpa.entities.Order;

import java.time.LocalDateTime;

public class UpdateOrderDTO {
    private Long customerId;
    private Long processorId;
    private Order.OrderStatus orderStatus;
    private LocalDateTime orderTimestamp;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Long processorId) {
        this.processorId = processorId;
    }

    public Order.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Order.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(LocalDateTime orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }
}