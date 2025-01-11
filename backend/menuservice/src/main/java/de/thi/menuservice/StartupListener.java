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
            cola.setPrice(4.50);

            Product beer = new Product();
            beer.setName("Bier");
            beer.setDescription("Hopfen und Malz");
            beer.setPrice(5.00);

            Product water = new Product();
            water.setName("Wasser");
            water.setDescription("Tafelwasser");
            water.setPrice(3.00);

            Product juice = new Product();
            juice.setName("Orangensaft");
            juice.setDescription("Wurde verschüttet");
            juice.setPrice(4.00);

            Product coffee = new Product();
            coffee.setName("Capuccino");
            coffee.setDescription("Kaffee mit Milchschaum");
            coffee.setPrice(3.50);

            drinks.setProducts(Arrays.asList(cola, beer, water, juice, coffee));
            categoryRepository.persist(drinks);

            Category meals = new Category();
            meals.setName("Speisen");

            Product schnitzel = new Product();
            schnitzel.setName("Schnitzel");
            schnitzel.setDescription("Paniertes Schnitzel mit Pommes");
            schnitzel.setPrice(17.50);

            Product burger = new Product();
            burger.setName("Burger");
            burger.setDescription("Saftiger Burger mit Pommes");
            burger.setPrice(16.50);

            Product salad = new Product();
            salad.setName("Salat");
            salad.setDescription("Gemischter Salat");
            salad.setPrice(8.00);

            Product schauefele = new Product();
            schauefele.setName("Schäufele");
            schauefele.setDescription("Schäufele mit Kloß und Soß");
            schauefele.setPrice(18.00);

            Product brotzeitplatte = new Product();
            brotzeitplatte.setName("Brotzeitplatte");
            brotzeitplatte.setDescription("Brotzeitplatte mit Wurst und Käse");
            brotzeitplatte.setPrice(12.00);

            meals.setProducts(Arrays.asList(schnitzel, burger, salad, schauefele, brotzeitplatte));
            categoryRepository.persist(meals);
        }
    }
}
