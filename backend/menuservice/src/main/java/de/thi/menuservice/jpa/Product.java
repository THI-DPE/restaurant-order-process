package de.thi.menuservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Product extends PanacheEntity {

    private String name;
    private String description;
    private double price;

}
