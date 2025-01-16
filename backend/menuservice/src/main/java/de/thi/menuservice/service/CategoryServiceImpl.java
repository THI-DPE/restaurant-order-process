package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.CategoryRepository;
import de.thi.menuservice.jpa.Product;
import de.thi.menuservice.jpa.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 *  CategoryServiceImpl ist eine Klasse, die die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Kategorien bereitstellt.
 *  @author Marvin Kern (unterstützt von GitHub Copilot)
 */

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das CategoryRepository-Objekt wird injiziert.
    @Inject
    CategoryRepository categoryRepository;

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das ProductRepository-Objekt wird injiziert.
    @Inject
    ProductRepository productRepository;

    public List<Category> findAll() {
        return categoryRepository.listAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // und Datenbank operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    @Transactional
    public void save(Category category) {
        categoryRepository.persist(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    // fügt ein Produkt zu einer Kategorie hinzu
    @Transactional
    public Category addProductToCategory(Long id, Product product) {
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory != null) {
            product.setCategory(existingCategory);
            productRepository.persist(product);
            existingCategory.getProducts().add(product);
            categoryRepository.persist(existingCategory);
            return existingCategory;
        }
        return null;
    }

}
