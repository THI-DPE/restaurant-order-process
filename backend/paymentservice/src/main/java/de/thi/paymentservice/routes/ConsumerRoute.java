package de.thi.paymentservice.routes;

import de.thi.paymentservice.jpa.Reimbursement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 *  Camel Route, die eingehende Nachrichten von der ActiveMQ-Warteschlange "reimbursement:incoming" konsumiert und sie als XML-Datei für "Bank" und "Paypal" speichert.
 *  @author Jannik Nüßlein (unterstützt von GitHub Copilot)
 */

// ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
// Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    // Override-Methode wird verwendet, um die Methode der Oberklasse zu überschreiben.
    // configure-Methode wird verwendet, um die Camel-Routen zu konfigurieren.
    @Override
    public void configure() throws Exception {

        from("activemq:reimbursementIncoming")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Reimbursement.class) // JSON deserialisieren
                .process(exchange -> {
                    Reimbursement reimbursement = exchange.getIn().getBody(Reimbursement.class);
                    persistPayment(reimbursement);
                    exchange.getIn().setHeader("orderId", reimbursement.getOrderId());
                })
                // Entscheidung, ob Order per Paypal oder Bank bezahlt wurde
                .choice()
                .when().simple("${body.paymentType} == 'PAYPAL'")
                .marshal().jacksonXml()
                .to("file:payment/paypal?fileName=${header.orderId}.xml")
                .log("Message with paymentType 'PAYPAL' saved as XML file.")
                .when().simple("${body.paymentType} == 'BANK'")
                .marshal().jacksonXml()
                .to("file:payment/bank?fileName=${header.orderId}.xml")
                .log("Message with paymentType 'BANK' saved as XML file.")
                .otherwise()
                .log("Message with other paymentType ignored.")
                .end();
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // Dadurch werden Datenbank-Operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    // Wenn eine Operation fehlschlägt, kann die Transaktion gerollbackt werden, um die Datenintegrität zu gewährleisten.
    @Transactional
    public void persistPayment(Reimbursement payment) {
        // Persist the Payment entity
        payment.persist();
    }
}