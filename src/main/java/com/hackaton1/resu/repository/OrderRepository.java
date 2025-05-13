package com.hackaton1.resu.repository;

import com.hackaton1.resu.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    
    // Custom query method to find orders by customer email
    List<Order> findByCustomerEmail(String email);
}