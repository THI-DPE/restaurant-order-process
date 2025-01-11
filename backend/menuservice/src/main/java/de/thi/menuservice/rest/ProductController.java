package de.thi.menuservice.rest;

import de.thi.menuservice.jpa.Product;
import de.thi.menuservice.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/products")
public class ProductController {

    private final ProductService productService;

    @Inject
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return Response.ok(product).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/total")
    public Response getProductDetailsAndTotalPrice(@QueryParam("id") List<Long> productIds) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = new ArrayList<>();
        double totalPrice = 0;
        for (Long productId : productIds) {
            Product product = productService.findById(productId);
            if (product != null) {
                totalPrice += product.getPrice();
                products.add(product);
            }
        }
        response.put("totalPrice", totalPrice);
        response.put("products", products);
        return Response.ok(response).build();
    }

}
