package de.thi.menuservice.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 *  Product Entity
 *  @author Marvin Kern
 */

@Entity
public class Product extends PanacheEntity {

    private String name;
    private String description;
    private double price;

    // ManyToOne bedeutet, dass viele Produkte zu einer Kategorie gehören.
    @ManyToOne
    // JsonIgnore ignoriert die Kategorie beim Serialisieren in JSON.
    @JsonIgnore
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
