package de.thi.orderservice.model;

/**
 *  Product ist eine Klasse, die Product vom MenuService repräsentiert.
 *  @author Alle
 */

// Record ist eine Klasse, die Product vom MenuService repräsentiert.
public record Product(Long id, String name, String description, double price) {
}
