package com.hackaton1.resu.listener;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class InventoryUpdateListenerTest {

    @Mock
    private ProductRepository productRepository;

    private InventoryUpdateListener listener;

    @BeforeEach
    void setUp() {
        listener = new InventoryUpdateListener(productRepository);
    }

    @Test
    void handleOrderCreatedEventShouldUpdateProductStock() {
        log.info("Starting test: handleOrderCreatedEventShouldUpdateProductStock");
        System.out.println("[DEBUG_LOG] Starting test: handleOrderCreatedEventShouldUpdateProductStock");

        // Arrange
        Product product1 = Product.builder()
                .id(1L)
                .name("Test Product 1")
                .price(100.0)
                .stock(10)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Test Product 2")
                .price(200.0)
                .stock(20)
                .build();

        log.info("Created test products: {} with stock {}, {} with stock {}", 
                product1.getName(), product1.getStock(),
                product2.getName(), product2.getStock());
        System.out.println("[DEBUG_LOG] Created test products: " + product1.getName() + " with stock " + product1.getStock() + 
                ", " + product2.getName() + " with stock " + product2.getStock());

        OrderItem orderItem1 = OrderItem.builder()
                .product(product1)
                .quantity(2)
                .price(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .product(product2)
                .quantity(3)
                .price(product2.getPrice())
                .build();

        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem1);
        items.add(orderItem2);

        Order order = Order.builder()
                .id(1L)
                .customerEmail("test@example.com")
                .items(items)
                .build();

        log.info("Created test order with ID: {} for customer: {}", order.getId(), order.getCustomerEmail());
        System.out.println("[DEBUG_LOG] Created test order with ID: " + order.getId() + " for customer: " + order.getCustomerEmail());

        OrderCreatedEvent event = new OrderCreatedEvent(this, order);

        // Act
        log.info("Calling handleOrderCreatedEvent");
        System.out.println("[DEBUG_LOG] Calling handleOrderCreatedEvent");
        listener.handleOrderCreatedEvent(event);

        // Assert
        log.info("Verifying product stocks after event handling");
        System.out.println("[DEBUG_LOG] Verifying product stocks after event handling");
        assertEquals(8, product1.getStock()); // 10 - 2 = 8
        assertEquals(17, product2.getStock()); // 20 - 3 = 17

        log.info("Product {} stock is now: {}", product1.getName(), product1.getStock());
        log.info("Product {} stock is now: {}", product2.getName(), product2.getStock());
        System.out.println("[DEBUG_LOG] Product " + product1.getName() + " stock is now: " + product1.getStock());
        System.out.println("[DEBUG_LOG] Product " + product2.getName() + " stock is now: " + product2.getStock());

        verify(productRepository, times(1)).save(product1);
        verify(productRepository, times(1)).save(product2);

        log.info("Test completed successfully");
        System.out.println("[DEBUG_LOG] Test completed successfully");
    }

    @Test
    void handleOrderCreatedEventShouldHandleInsufficientStock() {
        log.info("Starting test: handleOrderCreatedEventShouldHandleInsufficientStock");
        System.out.println("[DEBUG_LOG] Starting test: handleOrderCreatedEventShouldHandleInsufficientStock");

        // Arrange
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(100.0)
                .stock(1) // Only 1 in stock
                .build();

        log.info("Created test product: {} with stock {}", product.getName(), product.getStock());
        System.out.println("[DEBUG_LOG] Created test product: " + product.getName() + " with stock " + product.getStock());

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2) // Trying to order 2
                .price(product.getPrice())
                .build();

        log.info("Created order item with quantity {} (more than available stock)", orderItem.getQuantity());
        System.out.println("[DEBUG_LOG] Created order item with quantity " + orderItem.getQuantity() + " (more than available stock)");

        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);

        Order order = Order.builder()
                .id(1L)
                .customerEmail("test@example.com")
                .items(items)
                .build();

        log.info("Created test order with ID: {} for customer: {}", order.getId(), order.getCustomerEmail());
        System.out.println("[DEBUG_LOG] Created test order with ID: " + order.getId() + " for customer: " + order.getCustomerEmail());

        OrderCreatedEvent event = new OrderCreatedEvent(this, order);

        // Act
        log.info("Calling handleOrderCreatedEvent with insufficient stock");
        System.out.println("[DEBUG_LOG] Calling handleOrderCreatedEvent with insufficient stock");
        listener.handleOrderCreatedEvent(event);

        // Assert
        log.info("Verifying product stock remains unchanged");
        System.out.println("[DEBUG_LOG] Verifying product stock remains unchanged");
        assertEquals(1, product.getStock()); // Stock should remain unchanged

        log.info("Product {} stock is still: {}", product.getName(), product.getStock());
        System.out.println("[DEBUG_LOG] Product " + product.getName() + " stock is still: " + product.getStock());

        verify(productRepository, never()).save(product); // Product should not be saved

        log.info("Test completed successfully");
        System.out.println("[DEBUG_LOG] Test completed successfully");
    }
}
