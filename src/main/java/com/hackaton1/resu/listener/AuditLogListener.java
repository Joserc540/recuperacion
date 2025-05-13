package com.hackaton1.resu.listener;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Listener for OrderCreatedEvent that logs order details for auditing purposes.
 */
@Component
@Slf4j
public class AuditLogListener {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Async("taskExecutor")
    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        Order order = event.getOrder();
        
        log.info("=== AUDIT LOG: ORDER CREATED ===");
        log.info("Timestamp: {}", order.getCreatedAt().format(DATE_FORMATTER));
        log.info("Order ID: {}", order.getId());
        log.info("Customer Email: {}", order.getCustomerEmail());
        log.info("Items:");
        
        for (OrderItem item : order.getItems()) {
            log.info("  - Product: {} (ID: {})", item.getProduct().getName(), item.getProduct().getId());
            log.info("    Quantity: {}", item.getQuantity());
            log.info("    Price per unit: ${}", item.getPrice());
            log.info("    Subtotal: ${}", item.getSubtotal());
        }
        
        log.info("Total Order Value: ${}", order.getTotal());
        log.info("=== END AUDIT LOG ===");
    }
}