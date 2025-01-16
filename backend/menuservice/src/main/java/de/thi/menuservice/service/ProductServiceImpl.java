package de.thi.menuservice.service;

import de.thi.menuservice.jpa.Product;
import de.thi.menuservice.jpa.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 *  ProductServiceImpl ist eine Klasse, die die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Produkten bereitstellt.
 *  @author Marvin Kern
 */

//ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
//Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    //Die @Inject-Annotation wird verwendet, um die Abhängigkeiten zu injizieren. das ProductRepository-Objekt wird injiziert.
    @Inject
    ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.listAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id);
    }

    // @Transactional-Annotation stellt sicher, dass die Methode innerhalb Datenbank-Transaktion ausgeführt wird.
    // Dadurch werden Datenbank-Operationen wie persist, merge, remove, refresh, find, etc. werden innerhalb der Transaktion ausgeführt.
    // Wenn eine Operation fehlschlägt, kann die Transaktion gerollbackt werden, um die Datenintegrität zu gewährleisten.
    @Transactional
    public void save(Product product) {
        productRepository.persist(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
