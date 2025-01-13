package de.thi.menuservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

//PanacheEntity ist eine Klasse, die von Quarkus bereitgestellt wird und die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Entitäten bereitstellt.
@Entity
public class Category extends PanacheEntity {

    private String name;
    //CascadeType.ALL bedeutet, dass alle Operationen (Einfügen, Aktualisieren, Löschen) auf der Kategorie auch auf die Produkte angewendet werden.
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
