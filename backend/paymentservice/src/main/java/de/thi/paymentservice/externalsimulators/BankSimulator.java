package de.thi.paymentservice.externalsimulators;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

/**
 *  (Bank Simulation) Camel Route, die eingehende Nachrichten von der Datei "payment/bank" konsumiert und sie an die ActiveMQ-Queue "reimbursementProcessed" sendet.
 *  @author Jannik Nüßlein (unterstützt von GitHub Copilot)
 */

//Simuliert die Bank, die Zahlungen entgegennimmt und verarbeitet
//nimmt Dateien aus dem Verzeichnis payment/bank entgegen und sendet sie an die ActiveMQ-Queue reimbursementProcessed

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
// RouteBuilder ist eine Klasse, die von Camel bereitgestellt wird und die Methoden zum Konfigurieren von Camel-Routen bereitstellt.
public class BankSimulator extends RouteBuilder {

    //Override-Methode wird verwendet, um die Methode der Oberklasse zu überschreiben.
    //configure-Methode wird verwendet, um die Camel-Routen zu konfigurieren.
    @Override
    public void configure() throws Exception {
        from("file:payment/bank?delete=true")
                .log("File picked up: ${header.CamelFileName}")
                .to("activemq:reimbursementProcessed")
                .log("Message sent to ActiveMQ for processed payment.");
    }
}
