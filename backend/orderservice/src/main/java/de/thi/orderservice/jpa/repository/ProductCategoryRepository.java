package de.thi.orderservice.jpa.repository;

import de.thi.orderservice.jpa.entities.ProductCategory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductCategoryRepository implements PanacheRepository<ProductCategory> {

}
