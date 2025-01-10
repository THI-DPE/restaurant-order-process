package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.repository.OrderItemRepository;
import de.thi.orderservice.jpa.repository.OrderRepository;
import de.thi.orderservice.model.Product;
import de.thi.orderservice.rest.dto.OrderItemPriceDTO;
import de.thi.orderservice.service.external.MenuService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderItemService {

    @Inject
    OrderItemRepository orderItemRepository;

    @RestClient
    MenuService menuService;

    public Optional<OrderItem> findOrderItemById(Long productId) {
        return orderItemRepository.findByIdOptional(productId);
    }

    @Transactional
    public OrderItem updateOrderItem(Long id, OrderItem orderItem) {
        OrderItem existingOrderItem = orderItemRepository.findById(id);

        if (existingOrderItem != null) {
            if (orderItem.getProductId() != null) {
                existingOrderItem.setProductId(orderItem.getProductId());
            }
            if (orderItem.getOrderItemStatus() != null) {
                existingOrderItem.setOrderItemStatus(orderItem.getOrderItemStatus());
            }

            orderItemRepository.persist(existingOrderItem);
            return existingOrderItem;
        }
        return null;
    }

    @Transactional
    public List<OrderItem> getFailedOrderItemsByOrder(Long orderId) {
        Order order = Order.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order mit der ID " + orderId + " wurde nicht gefunden.");
        }

        return order.getProducts().stream()
                .flatMap(productCategory -> productCategory.getOrderItems().stream())
                .filter(orderItem -> orderItem.getOrderItemStatus() == OrderItem.OrderItemStatus.FAILED)
                .collect(Collectors.toList());
    }


    public List<OrderItemPriceDTO> getFailedOrderItems(Long id) {
        List<OrderItem> failedOrderItems = getFailedOrderItemsByOrder(id);
        List<OrderItemPriceDTO> failedOrderItemsDTO = new ArrayList<>();

        for (OrderItem item : failedOrderItems) {
            Product product = menuService.getProductById(item.getProductId());
            failedOrderItemsDTO.add(new OrderItemPriceDTO(item.getId(), product.name(), product.price()));
        }

        return failedOrderItemsDTO;
    }
}
