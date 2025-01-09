package de.thi.menuservice.rest;

import de.thi.menuservice.jpa.Category;
import de.thi.menuservice.jpa.CategoryRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/categories")
public class CategoryResource {

    private CategoryRepository categoryRepository;

    @Inject
    public CategoryResource(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GET
    public List<Category> getAllCategories() {
        return categoryRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @POST
    public void createCategory(Category category) {
        categoryRepository.persist(category);
    }

    @PUT
    @Path("/{id}")
    public void updateCategoryById(Long id, Category category) {
        categoryRepository.update(category.getName(), category.getProducts());
    }

}
