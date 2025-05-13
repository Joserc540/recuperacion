package com.hackaton1.resu.service;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, eventPublisher);
    }

    @Test
    void createOrderShouldSaveOrderAndPublishEvent() {
        // Arrange
        String customerEmail = "test@example.com";
        
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(100.0)
                .stock(10)
                .build();
        
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .price(product.getPrice())
                .build();
        
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        
        Order order = Order.builder()
                .customerEmail(customerEmail)
                .items(items)
                .build();
        
        Order savedOrder = Order.builder()
                .id(1L)
                .customerEmail(customerEmail)
                .items(items)
                .build();
        
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        
        // Act
        Order result = orderService.createOrder(order);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(customerEmail, result.getCustomerEmail());
        assertEquals(1, result.getItems().size());
        
        verify(orderRepository, times(1)).save(order);
        
        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        
        OrderCreatedEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(savedOrder, capturedEvent.getOrder());
    }
}