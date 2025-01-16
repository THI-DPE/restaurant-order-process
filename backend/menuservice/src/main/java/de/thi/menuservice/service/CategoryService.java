package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.Product;

import java.util.List;

/**
 *  CategoryService ist ein Interface, das die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Kategorien bereitstellt.
 *  @author Marvin Kern
 */

// CategoryService ist ein Interface, das die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Kategorien bereitstellt.
public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    void save(Category category);

    void delete(Long id);

    Category addProductToCategory(Long id, Product product);
}
