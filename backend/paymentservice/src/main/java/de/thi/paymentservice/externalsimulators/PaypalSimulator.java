package de.thi.paymentservice.externalsimulators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

//Simuliert Paypal, die Zahlungen entgegennimmt und verarbeitet
//nimmt Dateien aus dem Verzeichnis payment/paypal entgegen und sendet sie an die ActiveMQ-Queue reimbursementProcessed

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class PaypalSimulator extends RouteBuilder {

    //Override-Methode wird verwendet, um die Methode der Oberklasse zu überschreiben.
    //configure-Methode wird verwendet, um die Camel-Routen zu konfigurieren.
    @Override
    public void configure() throws Exception {
        from("file:payment/paypal?delete=true")
                .log("File picked up: ${header.CamelFileName}")
                .to("activemq:reimbursementProcessed")
                .log("Message sent to ActiveMQ for processed payment.");
    }
}
