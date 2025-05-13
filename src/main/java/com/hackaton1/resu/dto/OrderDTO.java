package com.hackaton1.resu.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Order entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    
    private Long id;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemDTO> items = new ArrayList<>();
    
    private LocalDateTime createdAt;
    
    private Double total;
}