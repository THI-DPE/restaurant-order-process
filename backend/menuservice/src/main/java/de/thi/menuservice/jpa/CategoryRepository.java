package de.thi.menuservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public Product findProductById(Long productId) {
        return getEntityManager()
                .createQuery("SELECT p FROM Category c JOIN c.products p WHERE p.id = :productId", Product.class)
                .setParameter("productId", productId)
                .getSingleResult();
    }

}
