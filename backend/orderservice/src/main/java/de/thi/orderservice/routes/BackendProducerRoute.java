/*package de.thi.orderservice.routes;

import org.apache.camel.builder.RouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BackendProducerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Lesen von Daten aus einem Dateisystem und in die JMS-Queue legen
        from("file://{{quarkus.camel.component.file.base-path}}?noop=true")
                .log("File picked up: ${header.CamelFileName}")
                .marshal().json() // Optionale Transformation
                .to("activemq:queue:process.input")
                .log("Message sent to ActiveMQ queue: process.input");
    }

}
*/