package de.thi.menuservice.rest;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.Product;
import de.thi.menuservice.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 *  Die Klasse CategoryController stellt die REST-Schnittstelle für die Produktkategorien bereit.
 *  @author Marvin Kern (unterstützt von GitHub Copilot)
 */

// Die @Path-Annotation wird verwendet, um den Pfad für die REST-Schnittstelle festzulegen.
@Path("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // Konstruktor, der CategoryService injiziert
    @Inject
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET-Anfrage, um alle Kategorien abzurufen
    @GET
    public Response getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return Response.ok(categories).build();
    }

    // GET-Anfrage, um eine Kategorie nach ID abzurufen
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            return Response.ok(category).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST-Anfrage, um eine Kategorie zu erstellen
    @POST
    public Response createCategory(Category category) {
        categoryService.save(category);
        return Response.ok(category).build();
    }

    // PUT-Anfrage, um eine Kategorie nach ID zu aktualisieren
    @PUT
    @Path("/{id}")
    public Response updateCategoryById(@PathParam("id") Long id, Category category) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory != null) {
            existingCategory.setName(category.getName());
            existingCategory.setProducts(category.getProducts());
            categoryService.save(existingCategory);
            return Response.ok(existingCategory).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST-Anfrage, um ein Produkt zu einer Kategorie hinzuzufügen
    @POST
    @Path("/{id}")
    public Response addProductToCategory(@PathParam("id") Long id, Product product) {
        Category category = categoryService.addProductToCategory(id, product);
        if (category != null) {
            return Response.ok(category).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}