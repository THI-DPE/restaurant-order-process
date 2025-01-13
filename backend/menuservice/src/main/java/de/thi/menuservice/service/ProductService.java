package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.Product;

import java.util.List;

// CategoryService ist ein Interface, das die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Kategorien bereitstellt.
public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    void save(Product product);

    void delete(Long id);

}
