package de.thi.orderservice.routes;

import de.thi.orderservice.jpa.entities.Notification;
import de.thi.orderservice.jpa.entities.dto.IncomingNotificationDTO;
import de.thi.orderservice.service.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.List;

@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    @Inject
    NotificationService notificationService;

    @Override
    public void configure() throws Exception {
        from("activemq:notifications")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, IncomingNotificationDTO.class)
                //.choice()
                //.when().simple("${body.messageType} == 'FAILED_ORDER_ITEMS'")
                //.to("direct:failedOrderItems")

                .process(exchange -> {
                    IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                    Notification notification = handleNotification(incomingNotification);
                    exchange.getIn().setBody(notification);
                })
                .marshal().json(JsonLibrary.Jackson)
                .to("file:notifications?fileName=${header.CamelFileName}.json");
    }

    public Notification handleNotification(IncomingNotificationDTO incomingNotification) {
        String messageType = incomingNotification.getMessageType().toString();
        String customerId = incomingNotification.getCustomerId();
        String orderId = incomingNotification.getOrderId();

        switch (messageType) {
            case "FAILED_ORDER_ITEMS":
                List<String> failedItems = incomingNotification.getFailedItems();
                return notificationService.getFailedOrderNotification(customerId, orderId, failedItems);
            case "PREPARATION_STARTED":
            case "PREPARATION_FINISHED":
                String productCategory = incomingNotification.getProductCategory();
                Notification.MessageType type = Notification.MessageType.valueOf(messageType);
                return notificationService.getOrderItemStatusNotification(customerId, orderId, productCategory, type);
            case "REIMBURSEMENT":
                double reimbursementAmount = incomingNotification.getReimbursementAmount();
                return notificationService.getReimbursementNotification(customerId, orderId, reimbursementAmount);
            default:
                throw new IllegalArgumentException("Unknown message type: " + messageType);
        }
    }

}
