package com.hackaton1.resu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Product price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;
    
    @NotNull(message = "Product stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;
    
    public void decreaseStock(int quantity) {
        if (this.stock >= quantity) {
            this.stock -= quantity;
        } else {
            throw new IllegalArgumentException("Not enough stock available for product: " + this.name);
        }
    }
}