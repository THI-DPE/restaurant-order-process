package de.thi.notificationservice.routes;

import de.thi.notificationservice.jpa.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Nachrichten von JMS-Queue lesen und basierend auf Inhalt routen
        from("activemq:queue:notifications")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Notification.class)
                .choice()
                .when(simple("${body.title} contains 'drinks'"))
                .marshal().jacksonXml()
                .to("file:notifications/drinks?fileName=${date:now:yyyyMMddHHmmssSSS}.xml")
                .log("Message with title 'drinks' saved as XML file.")
                .when(simple("${body.title} contains 'meals'"))
                .process(exchange -> {
                    Notification notification = exchange.getIn().getBody(Notification.class);
                    persistNotification(notification);
                })
                .log("Message with title 'meals' persisted to Database.")
                .otherwise()
                .log("Message does not match any criteria. No action taken.")
                .end();
    }

    @Transactional
    public void persistNotification(Notification notification) {
        // Persist the Ordering entity
        notification.persist();
    }
}