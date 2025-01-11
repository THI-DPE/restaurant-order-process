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
public class ProductServiceImpl implements ProductService {

    @Inject
    ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.listAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void save(Product product) {
        productRepository.persist(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
