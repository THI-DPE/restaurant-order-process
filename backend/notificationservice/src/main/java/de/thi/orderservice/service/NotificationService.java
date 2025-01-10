package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.FailedOrderItemsNotification;
import de.thi.orderservice.jpa.entities.Notification;
import de.thi.orderservice.jpa.entities.PreparationNotification;
import de.thi.orderservice.jpa.entities.ReimbursementNotification;
import de.thi.orderservice.jpa.repositories.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class NotificationService {

    @Inject
    NotificationRepository notificationRepository;


    public void sendFailedOrderNotification(String customerId, String orderId, List<String> failedItems) {

        FailedOrderItemsNotification notification = new FailedOrderItemsNotification();


        notification.setTitle("Wichtige Information zu Ihrer Bestellung");
        notification.setMessage("Folgende Artikel Ihrer Bestellung konnten nicht fertiggestellt werden: " + String.join(", ", failedItems));


        notification.setCustomerId(customerId);
        notification.setOrderId(orderId);
        notificationRepository.persist(notification);


    }

}
