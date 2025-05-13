package com.hackaton1.resu.service;

import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing products.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Get all products.
     *
     * @return list of all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get a product by ID.
     *
     * @param id the product ID
     * @return the product if found, otherwise empty
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Save a product.
     *
     * @param product the product to save
     * @return the saved product
     */
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Delete a product by ID.
     *
     * @param id the product ID
     */
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}