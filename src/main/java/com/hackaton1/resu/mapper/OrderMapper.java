package com.hackaton1.resu.mapper;

import com.hackaton1.resu.dto.CreateOrderRequest;
import com.hackaton1.resu.dto.OrderDTO;
import com.hackaton1.resu.dto.OrderItemDTO;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Order entities and DTOs.
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductService productService;

    /**
     * Convert Order entity to OrderDTO.
     *
     * @param order the order entity
     * @return the order DTO
     */
    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .customerEmail(order.getCustomerEmail())
                .items(itemDTOs)
                .createdAt(order.getCreatedAt())
                .total(order.getTotal())
                .build();
    }

    /**
     * Convert OrderItem entity to OrderItemDTO.
     *
     * @param orderItem the order item entity
     * @return the order item DTO
     */
    public OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subtotal(orderItem.getSubtotal())
                .build();
    }

    /**
     * Convert CreateOrderRequest to Order entity.
     *
     * @param request the create order request
     * @return the order entity
     */
    public Order toEntity(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = new Order();
        order.setCustomerEmail(request.getCustomerEmail());

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getProductById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + itemRequest.getProductId()));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            items.add(orderItem);
        }

        order.setItems(items);
        return order;
    }
}