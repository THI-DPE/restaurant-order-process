package de.thi.orderservice.dto;

import jakarta.persistence.Column;

import java.util.List;

/**
 *  IncomingNotificationDTO ist ein Data Transfer Object (DTO), das die eingehenden Benachrichtigungen enthält und die Eigenschaften der Benachrichtigung definiert.
 *  @author Alle
 */

// IncomingNotificationDTO ist eine Klasse, die die eingehenden Benachrichtigungen enthält und die Eigenschaften der Benachrichtigung definiert.
// Data transfer object (DTO) ist ein Entwurfsmuster, das verwendet wird, um eingehende Daten aufzunehmen und passend für den Service zur Verfügung zu stellen.
public class IncomingNotificationDTO {

    // nullable = false bedeutet, dass die Spalte nicht null sein kann.
    @Column(nullable = false)
    private MessageType messageType;

    // MessageType ist ein Enum, das die verschiedenen Arten von Benachrichtigungen definiert.
    public enum MessageType {
        REIMBURSEMENT,
        PREPARATION_STARTED,
        PREPARATION_FINISHED,
        FAILED_ORDER_ITEMS,
        ORDER_DELAYED,
        REIMBURSEMENT_DELAYED
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
