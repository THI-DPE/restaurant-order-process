package de.thi.paymentservice.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

//Produziert Nachrichten von der ActiveMQ-Warteschlange "reimbursement:processed" und leitet sie an den Prozess weiter

@ApplicationScoped
public class ProducerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Nachricht aus einem Backend-System lesen und in den Prozess leiten
        from("activemq:reimbursement:processed")
                .log("Message received from Backend: ${body}")
                .end();
                // TODO: Spiff implementieren
                //.to("direct:processInput");
    }
}