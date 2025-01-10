package de.thi.orderservice.service;

import de.thi.orderservice.jpa.entities.Order;
import de.thi.orderservice.jpa.entities.OrderItem;
import de.thi.orderservice.jpa.entities.ProductCategory;
import de.thi.orderservice.jpa.repository.OrderRepository;
import de.thi.orderservice.jpa.repository.ProductCategoryRepository;
import de.thi.orderservice.rest.dto.CreateOrderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    ProductCategoryRepository productCategoryRepository;

    public List<Order> findAll() {
        return orderRepository.listAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findByIdOptional(id);
    }

    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id);

        if (existingOrder != null) {
            if (order.getCustomerId() != null) {
                existingOrder.setCustomerId(order.getCustomerId());
            }
            if (order.getOrderTimestamp() != null) {
                existingOrder.setOrderTimestamp(order.getOrderTimestamp());
            }
            if (order.getProcessorId() != null) {
                existingOrder.setProcessorId(order.getProcessorId());
            }
            if (order.getStatus() != null) {
                existingOrder.setStatus(order.getStatus());
            }
                for (ProductCategory productCategory : existingOrder.getProducts()) {
                    Hibernate.initialize(productCategory.getOrderItems());
                    productCategoryRepository.persist(productCategory);
                }
                //TODO: ggf. noch Items aktualisieren nötig

                //existingOrder.setProducts(existingOrder.getProducts());

            orderRepository.persist(existingOrder);
            return existingOrder;
        }
        return null;
    }

    @Transactional
    public Order createOrder(CreateOrderDTO orderDTO) {
        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setOrderTimestamp(orderDTO.getOrderTimestamp());
        order.setProcessorId(null);
        order.setStatus(Order.OrderStatus.PROCESSING);

        List<ProductCategory> productCategories = new ArrayList<>();

        for (CreateOrderDTO.ProductCategoryDTO dto : orderDTO.getProducts()) {
            ProductCategory category = new ProductCategory();
            category.setProductCategoryName(dto.getProductCategoryName());

            List<OrderItem> orderItems = new ArrayList<>();
            if (dto.getProductIds() != null) {
                for (Long menuId : dto.getProductIds()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(menuId);
                    orderItem.setOrderItemStatus(OrderItem.OrderItemStatus.PROCESSING);
                    orderItems.add(orderItem);
                }
            }

            category.setOrderItems(orderItems);
            productCategories.add(category);
        }

        order.setProducts(productCategories);

        orderRepository.persist(order);
        return order;
    }

    @Transactional
    public boolean delete(Long id) {
        return orderRepository.deleteById(id);
    }

}