package com.hackaton1.resu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Product entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Product price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;
    
    @NotNull(message = "Product stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;
}