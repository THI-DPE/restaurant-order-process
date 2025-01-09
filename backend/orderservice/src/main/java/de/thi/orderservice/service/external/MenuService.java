package de.thi.orderservice.service.external;

import de.thi.orderservice.model.Category;
import de.thi.orderservice.model.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "menuservice")
public interface MenuService {

    @GET
    @Path("/categories")
    List<Category> getAllCategories();

    @GET
    @Path("/categories/{categoryId}")
    Category getCategoryById(@PathParam("categoryId") Long id);

    @GET
    @Path("/products/{productId}")
    Product getProductById(@PathParam("productId") Long id);

}
