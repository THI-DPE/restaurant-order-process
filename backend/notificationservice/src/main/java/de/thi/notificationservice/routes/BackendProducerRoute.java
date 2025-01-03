package de.thi.notificationservice.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class BackendProducerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Lesen von Daten aus einem Dateisystem und in die JMS-Queue legen
        from("file:notifications/drinks?noop=true")
                .log("File picked up: ${header.CamelFileName}")// Optionale Transformation
                .to("activemq:queue:testqueue")
                .log("Message sent to ActiveMQ queue: process.input");
    }

}
