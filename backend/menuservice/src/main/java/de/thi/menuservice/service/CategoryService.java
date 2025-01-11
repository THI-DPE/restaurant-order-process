package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.Product;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    void save(Category category);

    void delete(Long id);

    Category addProductToCategory(Long id, Product product);
}
