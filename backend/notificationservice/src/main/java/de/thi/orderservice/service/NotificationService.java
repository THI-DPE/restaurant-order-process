package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.FailedOrderItemsNotification;
import de.thi.orderservice.jpa.entities.Notification;
import de.thi.orderservice.jpa.entities.OrderItemStatusNotification;
import de.thi.orderservice.jpa.entities.ReimbursementNotification;
import de.thi.orderservice.jpa.repositories.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class NotificationService {

    @Inject
    NotificationRepository notificationRepository;

    public Notification getFailedOrderNotification(String customerId, String orderId, List<String> failedItems) {
        FailedOrderItemsNotification notification = new FailedOrderItemsNotification();

        notification.setCustomerId(customerId);
        notification.setOrderId(orderId);

        notification.setTitle("❗ Wichtige Information zu Ihrer Bestellung");
        notification.setMessage("Folgende Artikel Ihrer Bestellung konnten nicht fertiggestellt werden: " + String.join(", ", failedItems));

        notification.setFailedItems(failedItems);

        return notification;
    }

    public Notification getOrderItemStatusNotification(String customerId, String orderId, String productCategory, Notification.MessageType messageType) {
        OrderItemStatusNotification notification = new OrderItemStatusNotification();

        notification.setCustomerId(customerId);
        notification.setOrderId(orderId);

        String productCategoryText = productCategory.equals("drinks") ? "Getränke" : "Gerichte";

        if (messageType.equals(Notification.MessageType.PREPARATION_STARTED)) {
            notification.setTitle("⏳ Deine " + productCategoryText + " werden nun zubereitet");
            notification.setMessage("Ihre Bestellung ist nun in Bearbeitung und wird in Kürze fertiggestellt.");
        } else if (messageType.equals(Notification.MessageType.PREPARATION_FINISHED)) {
            notification.setTitle("🎉 " + productCategoryText + " fertiggestellt");
            notification.setMessage("Ihre Bestellung wurde fertiggestellt und kann nun abgeholt werden.");
        }

        notification.setProductCategory(productCategory);

        return notification;
    }

    public Notification getReimbursementNotification(String customerId, String orderId, double reimbursementAmount) {
        ReimbursementNotification notification = new ReimbursementNotification();

        notification.setCustomerId(customerId);
        notification.setOrderId(orderId);

        notification.setTitle("💸 Rückerstattung veranlasst");
        notification.setMessage("Ihre Rückerstattung in Höhe von " + reimbursementAmount + "€ wurde erfolgreich veranlasst und wird in Kürze auf die ursprüngliche Zahlungsmethode gutgeschrieben.");

        notification.setAmount(reimbursementAmount);

        return notification;
    }

}
