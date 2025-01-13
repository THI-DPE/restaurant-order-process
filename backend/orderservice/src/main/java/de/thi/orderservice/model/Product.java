package de.thi.orderservice.model;

// Record ist eine Klasse, die Product vom MenuService repräsentiert.
public record Product(Long id, String name, String description, double price) {
}
