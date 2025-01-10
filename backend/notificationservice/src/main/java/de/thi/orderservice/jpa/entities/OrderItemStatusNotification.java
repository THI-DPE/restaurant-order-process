package de.thi.orderservice.jpa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORDER_ITEM_STATUS")
public class OrderItemStatusNotification extends Notification {

    private String productCategory;

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

}