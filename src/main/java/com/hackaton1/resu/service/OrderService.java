package com.hackaton1.resu.service;

import com.hackaton1.resu.event.OrderCreatedEvent;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing orders.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Get all orders.
     *
     * @return list of all orders
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Get an order by ID.
     *
     * @param id the order ID
     * @return the order if found, otherwise empty
     */
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Get orders by customer email.
     *
     * @param email the customer email
     * @return list of orders for the customer
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    /**
     * Create a new order and publish an OrderCreatedEvent.
     *
     * @param order the order to create
     * @return the created order
     */
    @Transactional
    public Order createOrder(Order order) {
        log.info("Creating new order for customer: {}", order.getCustomerEmail());
        
        // Save the order to the database
        Order savedOrder = orderRepository.save(order);
        
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        
        // Publish the OrderCreatedEvent
        log.info("Publishing OrderCreatedEvent for order ID: {}", savedOrder.getId());
        eventPublisher.publishEvent(new OrderCreatedEvent(this, savedOrder));
        
        return savedOrder;
    }

    /**
     * Delete an order by ID.
     *
     * @param id the order ID
     */
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}