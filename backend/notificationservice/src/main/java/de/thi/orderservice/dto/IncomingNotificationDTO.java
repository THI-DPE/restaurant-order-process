package de.thi.orderservice.dto;

import jakarta.persistence.Column;

import java.util.List;

public class IncomingNotificationDTO {

    @Column(nullable = false)
    private MessageType messageType;

    public enum MessageType {
        REIMBURSEMENT,
        PREPARATION_STARTED,
        PREPARATION_FINISHED,
        FAILED_ORDER_ITEMS
    }
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String orderId;
    private List<String> failedItems;
    private String productCategory;
    private double reimbursementAmount;
    private String title;
    private String message;


    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
