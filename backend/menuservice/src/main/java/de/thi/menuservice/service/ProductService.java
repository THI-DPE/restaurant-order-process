package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    void save(Product product);

    void delete(Long id);

}
