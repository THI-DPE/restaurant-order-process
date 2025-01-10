package de.thi.orderservice.jpa.entities.dto;

import de.thi.orderservice.jpa.entities.Notification;

import java.util.List;

public class IncomingNotificationDTO {

    private Notification.MessageType messageType;
    private String customerId;
    private String orderId;
    private List<String> failedItems;
    private String productCategory;
    private double reimbursementAmount;

    public Notification.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(Notification.MessageType messageType) {
        this.messageType = messageType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getFailedItems() {
        return failedItems;
    }

    public void setFailedItems(List<String> failedItems) {
        this.failedItems = failedItems;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getReimbursementAmount() {
        return reimbursementAmount;
    }

    public void setReimbursementAmount(double reimbursementAmount) {
        this.reimbursementAmount = reimbursementAmount;
    }
}
