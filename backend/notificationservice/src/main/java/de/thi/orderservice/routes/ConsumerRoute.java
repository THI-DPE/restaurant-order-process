package de.thi.orderservice.routes;

import de.thi.orderservice.dto.IncomingNotificationDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    @Inject
    Template notification;

    @Override
    public void configure() throws Exception {
        from("activemq:notifications")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, IncomingNotificationDTO.class)
                .process(exchange -> {
                    IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                    exchange.getIn().setHeader("notificationDTO", incomingNotification);
                })
                .choice()
                    .when().simple("${header.notificationDTO.messageType} == 'FAILED_ORDER_ITEMS'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            List<String> failedItems = incomingNotification.getFailedItems();
                            incomingNotification.setTitle("❗ Wichtige Information zu Ihrer Bestellung!");
                            incomingNotification.setMessage("Folgende Artikel Ihrer Bestellung konnten nicht fertiggestellt werden: " + String.join(", ", failedItems));
                        })
                    .when().simple("${header.notificationDTO.messageType} == 'PREPARATION_STARTED'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            String productCategoryText = incomingNotification.getProductCategory().equals("drinks") ? "Getränke" : "Gerichte";
                            incomingNotification.setTitle("⏳ Deine " + productCategoryText + " werden nun zubereitet!");
                            incomingNotification.setMessage("Ihre Bestellung ist nun in Bearbeitung und wird in Kürze fertiggestellt.");
                        })
                    .when().simple("${header.notificationDTO.messageType} == 'PREPARATION_FINISHED'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            String productCategoryText = incomingNotification.getProductCategory().equals("drinks") ? "Getränke" : "Gerichte";
                            incomingNotification.setTitle("🎉 " + productCategoryText + " fertiggestellt!");
                            incomingNotification.setMessage("Ihre Bestellung wurde fertiggestellt und kann nun abgeholt werden.");
                        })
                    .when().simple("${header.notificationDTO.messageType} == 'REIMBURSEMENT'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
                            String formattedAmount = numberFormat.format(incomingNotification.getReimbursementAmount());
                            incomingNotification.setTitle("💸 Rückerstattung veranlasst!");
                            incomingNotification.setMessage("Ihre Rückerstattung in Höhe von " + formattedAmount + "€ wurde erfolgreich veranlasst und wird in Kürze auf die ursprüngliche Zahlungsmethode gutgeschrieben.");
                        })
                    .otherwise()
                        .log("Unknown message type: ${header.notificationDTO.messageType}")
                .end()
                .process(exchange -> {
                    IncomingNotificationDTO incomingNotification = exchange.getIn().getHeader("notificationDTO", IncomingNotificationDTO.class);
                    String customerId = incomingNotification.getCustomerId();
                    String orderId = incomingNotification.getOrderId();
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    exchange.getIn().setHeader("customerId", "Customer_" + customerId);
                    exchange.getIn().setHeader("CamelFileName", "Order_" + orderId + "_" + timestamp + ".html");
                    TemplateInstance templateInstance = notification.data("title", incomingNotification.getTitle())
                            .data("message", incomingNotification.getMessage());
                    String htmlBody = templateInstance.render();
                    exchange.getIn().setBody(htmlBody);
                })
                .log("Processed message")
                .choice()
                    .when().simple("${header.notificationDTO.messageType} == 'REIMBURSEMENT'")
                        .to("file:notifications/emails?fileName=${header.customerId}/${header.CamelFileName}")
                    .otherwise()
                        .to("file:notifications/push?fileName=${header.customerId}/${header.CamelFileName}")
                .end();
    }
}
