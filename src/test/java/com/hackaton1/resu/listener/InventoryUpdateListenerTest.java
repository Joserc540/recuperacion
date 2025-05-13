package com.hackaton1.resu.listener;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.repository.ProductRepository;
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
        
        OrderCreatedEvent event = new OrderCreatedEvent(this, order);
        
        // Act
        listener.handleOrderCreatedEvent(event);
        
        // Assert
        assertEquals(8, product1.getStock()); // 10 - 2 = 8
        assertEquals(17, product2.getStock()); // 20 - 3 = 17
        
        verify(productRepository, times(1)).save(product1);
        verify(productRepository, times(1)).save(product2);
    }
    
    @Test
    void handleOrderCreatedEventShouldHandleInsufficientStock() {
        // Arrange
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(100.0)
                .stock(1) // Only 1 in stock
                .build();
        
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2) // Trying to order 2
                .price(product.getPrice())
                .build();
        
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        
        Order order = Order.builder()
                .id(1L)
                .customerEmail("test@example.com")
                .items(items)
                .build();
        
        OrderCreatedEvent event = new OrderCreatedEvent(this, order);
        
        // Act
        listener.handleOrderCreatedEvent(event);
        
        // Assert
        assertEquals(1, product.getStock()); // Stock should remain unchanged
        verify(productRepository, never()).save(product); // Product should not be saved
    }
}