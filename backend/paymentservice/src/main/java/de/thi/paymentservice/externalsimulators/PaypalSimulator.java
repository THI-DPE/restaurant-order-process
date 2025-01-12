package de.thi.paymentservice.externalsimulators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

//Simuliert Paypal, was Zahlungen entgegennimmt und verarbeitet
@ApplicationScoped
public class PaypalSimulator extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:payment/paypal?delete=true")
                .log("File picked up: ${header.CamelFileName}")
                .to("activemq:reimbursementProcessed")
                .log("Message sent to ActiveMQ for processed payment.");
    }

}
