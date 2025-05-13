package com.hackaton1.resu.listener;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener for OrderCreatedEvent that sends an email notification to the customer.
 * The email sending is simulated with logs.
 */
@Component
@Slf4j
public class EmailNotificationListener {

    @Async("taskExecutor")
    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        Order order = event.getOrder();
        
        log.info("Sending email notification to customer: {}", order.getCustomerEmail());
        log.info("Email Subject: Your Order #{} has been confirmed", order.getId());
        
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Dear Customer,\n\n");
        emailBody.append("Thank you for your order. Your order details are as follows:\n\n");
        emailBody.append("Order ID: ").append(order.getId()).append("\n");
        emailBody.append("Order Date: ").append(order.getCreatedAt()).append("\n\n");
        emailBody.append("Items:\n");
        
        for (OrderItem item : order.getItems()) {
            emailBody.append("- ")
                    .append(item.getQuantity())
                    .append(" x ")
                    .append(item.getProduct().getName())
                    .append(" (")
                    .append(item.getPrice())
                    .append(" each): $")
                    .append(item.getSubtotal())
                    .append("\n");
        }
        
        emailBody.append("\nTotal: $").append(order.getTotal()).append("\n\n");
        emailBody.append("Thank you for shopping with us!\n");
        emailBody.append("The Team");
        
        log.info("Email Body: \n{}", emailBody.toString());
        log.info("Email notification sent successfully to {}", order.getCustomerEmail());
    }
}