package de.thi.orderservice.jpa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import java.util.List;

@Entity
@DiscriminatorValue("FAILED_ORDER_ITEMS")
public class FailedOrderItemsNotification extends Notification {

    @ElementCollection
    private List<String> failedItems;

    public List<String> getFailedItems() {
        return failedItems;
    }

    public void setFailedItems(List<String> failedItems) {
        this.failedItems = failedItems;
    }

}