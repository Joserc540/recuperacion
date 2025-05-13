package com.hackaton1.resu.listener;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener for OrderCreatedEvent that updates the inventory by reducing the stock
 * of products in the order.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryUpdateListener {

    private final ProductRepository productRepository;

    @Async("taskExecutor")
    @EventListener
    @Transactional
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        Order order = event.getOrder();
        
        log.info("Updating inventory for order: {}", order.getId());
        
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            
            log.info("Reducing stock for product: {} (ID: {}) by {} units", 
                    product.getName(), product.getId(), quantity);
            
            try {
                product.decreaseStock(quantity);
                productRepository.save(product);
                log.info("Stock updated successfully. New stock for product {}: {}", 
                        product.getName(), product.getStock());
            } catch (IllegalArgumentException e) {
                log.error("Failed to update stock for product: {} (ID: {}). Reason: {}", 
                        product.getName(), product.getId(), e.getMessage());
                // In a real application, we might want to handle this error differently,
                // such as canceling the order or notifying an administrator
            }
        }
        
        log.info("Inventory update completed for order: {}", order.getId());
    }
}