package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.CategoryRepository;
import de.thi.menuservice.jpa.Product;
import de.thi.menuservice.jpa.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    ProductRepository productRepository;

    public List<Category> findAll() {
        return categoryRepository.listAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public void save(Category category) {
        categoryRepository.persist(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

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
