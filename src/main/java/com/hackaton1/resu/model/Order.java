package com.hackaton1.resu.model;

import jakarta.persistence.*;
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

@Entity
@Table(name = "orders") // "order" is a reserved keyword in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItem> items = new ArrayList<>();
    
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    
    public Double getTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
}