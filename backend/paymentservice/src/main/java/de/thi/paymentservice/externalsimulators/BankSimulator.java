package de.thi.paymentservice.externalsimulators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

//Simuliert die Bank, die Zahlungen entgegennimmt und verarbeitet
@ApplicationScoped
public class BankSimulator extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:payment/bank?")
                .log("File picked up: ${header.CamelFileName}")
                .to("activemq:reimbursementProcessed")
                .log("Message sent to ActiveMQ for processed payment.");
    }

}
