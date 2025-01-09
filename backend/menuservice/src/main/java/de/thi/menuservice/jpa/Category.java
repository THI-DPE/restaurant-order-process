package de.thi.menuservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Category extends PanacheEntity {

    private String name;
    private List<Product> products;

}
