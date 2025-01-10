package de.thi.paymentservice.externalsimulators;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

//Simuliert die Bank, die Zahlungen entgegennimmt und verarbeitet
@ApplicationScoped
public class BankSimulator extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Lesen von Daten aus einem Dateisystem und in die JMS-Queue legen
        from("file:payment/bank?delete=true")
                .log("File picked up: ${header.CamelFileName}")
                .to("activemq:reimbursement:processed")
                .log("Message sent to ActiveMQ queue: reimbursement:processed");
    }

}
