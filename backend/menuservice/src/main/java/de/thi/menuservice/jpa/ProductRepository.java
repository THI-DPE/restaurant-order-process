package de.thi.menuservice.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 *  Product Repository
 *  @author Marvin Kern
 */

// PanacheRepository ist eine Klasse, die von Quarkus bereitgestellt wird und die Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Entitäten bereitstellt.
// ApplicationScoped ist eine Annotation, die von Quarkus bereitgestellt wird und die Lebensdauer der Klasse steuert.
// Eine Klasse, die mit @ApplicationScoped annotiert ist, wird einmal pro Anwendung erstellt und verwaltet.
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

}
