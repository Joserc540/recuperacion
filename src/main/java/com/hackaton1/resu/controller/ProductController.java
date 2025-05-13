package com.hackaton1.resu.controller;

import com.hackaton1.resu.dto.ProductDTO;
import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * GET /products : Get all products.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.debug("REST request to get all Products");
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    /**
     * GET /products/{id} : Get the "id" product.
     *
     * @param id the id of the product to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the product, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        return productService.getProductById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /products : Create a new product.
     *
     * @param productDTO the product to create
     * @return the ResponseEntity with status 201 (Created) and with body the new product
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.debug("REST request to create Product : {}", productDTO);
        
        Product product = convertToEntity(productDTO);
        Product result = productService.saveProduct(product);
        ProductDTO responseDTO = convertToDTO(result);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * PUT /products/{id} : Updates an existing product.
     *
     * @param id the id of the product to update
     * @param productDTO the product to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated product,
     * or with status 404 (Not Found) if the product could not be found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        log.debug("REST request to update Product : {}, {}", id, productDTO);
        
        if (!productService.getProductById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        productDTO.setId(id);
        Product product = convertToEntity(productDTO);
        Product result = productService.saveProduct(product);
        ProductDTO responseDTO = convertToDTO(result);
        
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * DELETE /products/{id} : delete the "id" product.
     *
     * @param id the id of the product to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convert Product entity to ProductDTO.
     *
     * @param product the product entity
     * @return the product DTO
     */
    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    /**
     * Convert ProductDTO to Product entity.
     *
     * @param productDTO the product DTO
     * @return the product entity
     */
    private Product convertToEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .build();
    }
}