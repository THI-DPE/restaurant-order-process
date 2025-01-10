package de.thi.paymentservice.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

//Produziert Nachrichten von der ActiveMQ-Warteschlange "reimbursement:processed" und leitet sie an den Prozess weiter

@ApplicationScoped
public class ProducerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Nachricht aus einem Backend-System lesen und in den Prozess leiten
        from("activemq:reimbursement:processed")
                .log("Message received from Backend: ${body}")
                .unmarshal().jacksonXml() // Unmarshal from XML
                .marshal().json(JsonLibrary.Jackson) // Marshal to JSON
                .log("Converted message to JSON: ${body}")
                .end();
                // TODO: Spiff implementieren
                // Post an interne spiff-Adresse
                //.to("direct:processInput");
    }
}