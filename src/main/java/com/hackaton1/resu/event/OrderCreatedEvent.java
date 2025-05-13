package com.hackaton1.resu.event;

import com.hackaton1.resu.model.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is published when a new order is created.
 * Contains information about the order such as:
 * - Order ID
 * - Customer email
 * - List of products
 */
@Getter
public class OrderCreatedEvent extends ApplicationEvent {
    
    private final Order order;
    
    public OrderCreatedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
    
    /**
     * Get the ID of the order.
     * @return the order ID
     */
    public Long getOrderId() {
        return order.getId();
    }
    
    /**
     * Get the email of the customer.
     * @return the customer email
     */
    public String getCustomerEmail() {
        return order.getCustomerEmail();
    }
}