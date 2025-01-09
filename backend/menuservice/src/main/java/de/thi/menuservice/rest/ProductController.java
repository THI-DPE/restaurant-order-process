package de.thi.menuservice.rest;

import de.thi.menuservice.jpa.Product;
import de.thi.menuservice.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/products")
public class ProductController {

    // TODO: Eigenes ProductRepository

    private final CategoryService categoryService;

    @Inject
    public ProductController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GET
    @Path("/{id}")
    public Product getProductById(@PathParam("id") Long id) {
        return categoryService.findProductById(id);
    }

}
