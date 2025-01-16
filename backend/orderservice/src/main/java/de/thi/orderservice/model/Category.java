package de.thi.orderservice.model;

import java.util.List;

/**
 *  Category ist eine Klasse, die Category vom MenuService repräsentiert.
 *  @author Alle
 */

// Record ist eine Klasse, die Category vom MenuService repräsentiert.
public record Category(Long id, String name, List<Product> products) {
}
