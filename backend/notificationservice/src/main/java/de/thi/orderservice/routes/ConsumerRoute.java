package de.thi.orderservice.routes;

import de.thi.orderservice.jpa.entities.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:notifications")
                .log("Received message: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Notification.class)

                .choice()
                .when().simple("${body.messageType} == ''")
                .marshal().jacksonXml()
                .to("file:payment/paypal?fileName=${header.orderId}.xml")
                .log("Message with paymentType 'PAYPAL' saved as XML file.")
                .when().simple("${body.productCategory} == 'drink'")
                .marshal().jacksonXml()
                .to("file:payment/bank?fileName=${header.orderId}.xml")
                .log("Message with paymentType 'BANK' saved as XML file.")
                .otherwise()
                .log("Message with other paymentType ignored.")
                .end();
    }

    @Transactional
    public void persistNotification(Notification notification) {
        notification.persist();
    }
}
