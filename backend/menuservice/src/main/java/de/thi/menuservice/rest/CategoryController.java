package de.thi.menuservice.rest;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.service.CategoryService;
import de.thi.menuservice.service.CategoryServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Inject
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GET
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GET
    @Path("/{id}")
    public Category getCategoryById(@PathParam("id") Long id) {
        return categoryService.findById(id);
    }

    @POST
    public void createCategory(Category category) {
        categoryService.save(category);
    }

    @PUT
    @Path("/{id}")
    public void updateCategoryById(@PathParam("id") Long id, Category category) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory != null) {
            existingCategory.setName(category.getName());
            existingCategory.setProducts(category.getProducts());
            categoryService.save(existingCategory);
        }
    }
}