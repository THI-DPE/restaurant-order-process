package de.thi.orderservice.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderDTO {

    private Long customerId;
    private LocalDateTime orderTimestamp;
    private List<ProductCategoryDTO> products;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(LocalDateTime orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public List<ProductCategoryDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCategoryDTO> products) {
        this.products = products;
    }

    public static class ProductCategoryDTO {
        private String productCategoryName;
        private List<Long> productIds;

        public String getProductCategoryName() {
            return productCategoryName;
        }

        public void setProductCategoryName(String productCategoryName) {
            this.productCategoryName = productCategoryName;
        }

        public List<Long> getProductIds() {
            return productIds;
        }

        public void setProductIds(List<Long> productIds) {
            this.productIds = productIds;
        }
    }
}