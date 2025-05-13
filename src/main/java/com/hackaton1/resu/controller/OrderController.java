package com.hackaton1.resu.controller;

import com.hackaton1.resu.dto.CreateOrderRequest;
import com.hackaton1.resu.dto.OrderDTO;
import com.hackaton1.resu.mapper.OrderMapper;
import com.hackaton1.resu.model.Order;
import com.hackaton1.resu.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing orders.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    /**
     * GET /orders : Get all orders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.debug("REST request to get all Orders");
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    /**
     * GET /orders/{id} : Get the "id" order.
     *
     * @param id the id of the order to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the order, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        return orderService.getOrderById(id)
                .map(orderMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /orders : Create a new order.
     *
     * @param request the order to create
     * @return the ResponseEntity with status 201 (Created) and with body the new order
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.debug("REST request to create Order : {}", request);
        
        try {
            Order order = orderMapper.toEntity(request);
            Order result = orderService.createOrder(order);
            OrderDTO responseDTO = orderMapper.toDTO(result);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create order", e);
            return ResponseEntity.badRequest().build();
        }
    }
}