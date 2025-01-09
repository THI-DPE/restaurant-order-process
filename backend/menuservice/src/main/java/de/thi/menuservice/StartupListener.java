package de.thi.menuservice;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.CategoryRepository;
import de.thi.menuservice.jpa.Product;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Arrays;

public class StartupListener {

    @Inject
    CategoryRepository categoryRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if (categoryRepository.count() == 0) {
            Category drinks = new Category();
            drinks.setName("Getränke");

            Product cola = new Product();
            cola.setName("Cola");
            cola.setDescription("Eiskalt und erfrischend");
            cola.setPrice(1.50);

            Product beer = new Product();
            beer.setName("Bier");
            beer.setDescription("Hopfen und Malz");
            beer.setPrice(2.00);

            drinks.setProducts(Arrays.asList(cola, beer));
            categoryRepository.persist(drinks);

            Category meals = new Category();
            meals.setName("Speisen");

            Product schnitzel = new Product();
            schnitzel.setName("Schnitzel");
            schnitzel.setDescription("Paniertes Schnitzel mit Pommes");
            schnitzel.setPrice(7.50);

            Product burger = new Product();
            burger.setName("Burger");
            burger.setDescription("Saftiger Burger mit Pommes");
            burger.setPrice(6.50);

            meals.setProducts(Arrays.asList(schnitzel, burger));
            categoryRepository.persist(meals);
        }
    }
}
