package de.thi.orderservice.rest.dto;

public class OrderItemPriceDTO {
    private Long orderItemId;
    private String name;
    private Double price;

    public OrderItemPriceDTO(Long orderItemId, String name, Double price) {
        this.orderItemId = orderItemId;
        this.name = name;
        this.price = price;
    }

    public Long getorderItemId() {
        return orderItemId;
    }

    public void setorderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}