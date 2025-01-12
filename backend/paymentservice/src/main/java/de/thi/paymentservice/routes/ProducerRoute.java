package de.thi.paymentservice.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Map;

//Produziert Nachrichten von der ActiveMQ-Warteschlange "reimbursement:processed" und leitet sie an den Prozess weiter

@ApplicationScoped
public class ProducerRoute extends RouteBuilder {

    @ConfigProperty(name = "target.url")
    String targetUrl;

    @Override
    public void configure() throws Exception {
        // Nachricht aus einem Backend-System lesen und in den Prozess leiten
        from("activemq:reimbursementProcessed")
                .log("Message received from Backend: ${body}")
                .unmarshal().jacksonXml() // Unmarshal from XML
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Map<String, Object> body = exchange.getIn().getBody(Map.class);
                        body.put("timestamp", Instant.now().toString());
                        body.put("status", "REIMBURSEMENT_SUCCESSFUL");
                        exchange.getIn().setBody(body);
                    }
                })
                .marshal().json(JsonLibrary.Jackson) // Marshal to JSON
                .log("Converted message to JSON: ${body}")
                .setHeader("Content-Type", constant("application/json"))
                .toD(targetUrl + "?httpMethod=POST")
                .log("Message sent to " + targetUrl);
    }
}