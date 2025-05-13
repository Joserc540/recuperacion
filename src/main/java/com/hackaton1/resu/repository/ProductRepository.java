package com.hackaton1.resu.repository;

import com.hackaton1.resu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // We can add custom query methods here if needed
}