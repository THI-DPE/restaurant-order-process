package de.thi.orderservice.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

// CreateOrderDTO ist eine Klasse, die die Daten für die Erstellung einer Bestellung enthält. (payed Order)
//data transfer object (DTO) ist ein Entwurfsmuster, das verwendet wird, um eingehende Daten aufzunehmen und passend für den Service zur Verfügung zu stellen.
public class CreateOrderDTO {

    private Long customerId;
    private LocalDateTime orderTimestamp;
    private List<ProductCategoryDTO> products;
    private String paymentType;
    private String paymentDetails;

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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    // ProductCategoryDTO ist eine Klasse, die die Produktkategorie und die Bestellpositionen enthält.
    public static class ProductCategoryDTO {
        private String productCategoryName;
        private List<OrderItemDTO> orderItems;

        public String getProductCategoryName() {
            return productCategoryName;
        }

        public void setProductCategoryName(String productCategoryName) {
            this.productCategoryName = productCategoryName;
        }

        public List<OrderItemDTO> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItemDTO> orderItems) {
            this.orderItems = orderItems;
        }

        // OrderItemDTO ist eine Klasse, die die Bestellpositionen enthält.
        public static class OrderItemDTO {
            private Long productId;
            private String remark;

            public Long getProductId() {
                return productId;
            }

            public void setProductId(Long productId) {
                this.productId = productId;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }
    }
}