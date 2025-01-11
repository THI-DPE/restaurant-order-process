package de.thi.orderservice.model;

import java.util.List;

public record Category(Long id, String name, List<Product> products) {
}
