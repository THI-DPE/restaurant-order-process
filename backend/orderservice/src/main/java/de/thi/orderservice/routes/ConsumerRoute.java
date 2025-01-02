package de.thi.orderservice.routes;

import de.thi.orderservice.jpa.Ordering;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Nachrichten von JMS-Queue lesen und basierend auf Inhalt routen
        from("activemq:queue:payed.order")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Ordering.class)
                .process(exchange -> {
                    Ordering ordering = exchange.getIn().getBody(Ordering.class);
                    persistOrdering(ordering);
                })
                .log("Routed to Database");
    }

    @Transactional
    public void persistOrdering(Ordering ordering) {
        // Persist the Ordering entity
        ordering.persist();
    }
}