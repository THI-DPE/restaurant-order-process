package de.thi.paymentservice.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Map;
//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.

//Produziert Nachrichten von der ActiveMQ-Warteschlange "reimbursement:processed" und leitet sie an den Prozess weiter
@ApplicationScoped
public class ProducerRoute extends RouteBuilder {

    @ConfigProperty(name = "target.url")
    String targetUrl;

    @ConfigProperty(name = "spiffworkflow.api.key")
    String apiKey;

    //Override-Methode wird verwendet, um die Methode der Oberklasse zu überschreiben.
    //configure-Methode wird verwendet, um die Camel-Routen zu konfigurieren.
    @Override
    public void configure() throws Exception {
        // Nachricht aus einem Backend-System lesen und in den Prozess leiten
        //from definiert Herkunft (in unserem Fall die ActiveMQ-Queue "reimbursementProcessed")
        from("activemq:reimbursementProcessed")
                .log("Message received from Backend: ${body}")
                .unmarshal().jacksonXml()
                //Setzen von timestamp und status in die Erstattunung
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
                //Setzen von Headern für Komunikation mit Spiffworkflow
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Spiffworkflow-Api-Key", constant(apiKey))
                .setHeader("Content-Length", simple("${body.length()}"))
                .setHeader("Accept", constant("*/*"))
                .toD(targetUrl + "?httpMethod=POST")
                .log("Message sent to " + targetUrl);
    }
}