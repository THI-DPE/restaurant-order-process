package de.thi.orderservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Ordering extends PanacheEntity {

    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
