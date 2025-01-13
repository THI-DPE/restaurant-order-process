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
import java.util.UUID;

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
//ConsumerRoute ist eine Klasse, die die eingehenden Benachrichtigungen mit Camel verarbeitet und die Benachrichtigungen an die entsprechenden Kanäle weiterleitet (mail oder push).
@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das Template-Objekt aus /resources/templates/notification.html wird injiziert.
    @Inject
    Template notification;

    //Override-Methode wird verwendet, um die Methode der Oberklasse zu überschreiben.
    //configure-Methode wird verwendet, um die Camel-Routen zu konfigurieren.
    @Override
    public void configure() throws Exception {
        //from definiert Herkunft (in unserem Fall die ActiveMQ-Queue "notifications")
        from("activemq:notifications")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, IncomingNotificationDTO.class)
                //process unterbricht camel und führt eine beliebige Logik aus
                .process(exchange -> {
                    IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                    exchange.getIn().setHeader("notificationDTO", incomingNotification);
                })
                //Bilden von verschiedenen Benachrichtigungen basierend auf dem Typ der eingehenden Benachrichtigung
                .choice()
                    .when().simple("${header.notificationDTO.messageType} == 'FAILED_ORDER_ITEMS'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            List<String> failedItems = incomingNotification.getFailedItems();
                            incomingNotification.setTitle("❗ Wichtige Information zu Ihrer Bestellung!");
                            incomingNotification.setMessage("Folgende Artikel Ihrer Bestellung konnten nicht fertiggestellt werden: " + String.join(", ", failedItems));
                        })
                    .when().simple("${header.notificationDTO.messageType} == 'ORDER_DELAYED'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            String productCategoryText = incomingNotification.getProductCategory().equals("drinks") ? "Getränke" : "Gerichte";
                            incomingNotification.setTitle("⏳ Ihre " + productCategoryText + " verzögern sich!");
                            incomingNotification.setMessage("Ihre "+ productCategoryText+" wird sich verzögern. Wir bitten um Ihr Verständnis.");
                        })
                    .when().simple("${header.notificationDTO.messageType} == 'REIMBURSEMENT_DELAYED'")
                        .process(exchange -> {
                            IncomingNotificationDTO incomingNotification = exchange.getIn().getBody(IncomingNotificationDTO.class);
                            incomingNotification.setTitle("💸 Rückerstattung verzögert!");
                            incomingNotification.setMessage("Ihre Rückerstattung wird sich verzögern. Wir bitten um Ihr Verständnis.");
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
                //Erstellen der HTML-Datei mit den Benachrichtigungsinformationen
                .process(exchange -> {
                    IncomingNotificationDTO incomingNotification = exchange.getIn().getHeader("notificationDTO", IncomingNotificationDTO.class);
                    String customerId = incomingNotification.getCustomerId();
                    String orderId = incomingNotification.getOrderId();
                    String uniqueId = UUID.randomUUID().toString();
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    exchange.getIn().setHeader("customerId", "Customer_" + customerId);
                    exchange.getIn().setHeader("CamelFileName", "Order_" + orderId + "_"+ incomingNotification.getMessageType()+ "_" + timestamp + "_" + uniqueId + ".html");
                    TemplateInstance templateInstance = notification.data("title", incomingNotification.getTitle())
                            .data("message", incomingNotification.getMessage());
                    String htmlBody = templateInstance.render();
                    exchange.getIn().setBody(htmlBody);
                })
                .log("Processed message")
                //Weiterleitung der Benachrichtigung an den entsprechenden Kanal (mail oder push); In unserem Fall wird Email und Push-Notification mit Ordnern simuliert
                .choice()
                    .when().simple("${header.notificationDTO.messageType} contains 'REIMBURSEMENT'")
                        .to("file:notifications/emails?fileName=${header.customerId}/${header.CamelFileName}")
                    .otherwise()
                        .to("file:notifications/push?fileName=${header.customerId}/${header.CamelFileName}")
                .end();
    }
}
