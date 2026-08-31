 package org.example.jpaentityrelationships.controller;

import org.example.jpaentityrelationships.dto.OrderRequest;
import org.example.jpaentityrelationships.dto.OrderResponse;
import org.example.jpaentityrelationships.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // CREATE ORDER
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request) {

        OrderResponse response =
                orderService.createOrder(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long id) {

        OrderResponse response =
                orderService.getOrder(id);

        return ResponseEntity.ok(response);
    }

    // GET ALL ORDERS FOR USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(
            @PathVariable Long userId) {

        List<OrderResponse> response =
                orderService.getOrdersByUser(userId);

        return ResponseEntity.ok(response);
    }
}

