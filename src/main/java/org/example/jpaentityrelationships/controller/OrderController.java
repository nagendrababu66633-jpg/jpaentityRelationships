package org.example.jpaentityrelationships.controller;

import org.example.jpaentityrelationships.dto.OrderRequest;
import org.example.jpaentityrelationships.dto.OrderResponse;
import org.example.jpaentityrelationships.dto.OrderStatisticsResponse;
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

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET MY ORDERS
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {

        List<OrderResponse> response = orderService.getMyOrders();

        return ResponseEntity.ok(response);
    }

    // GET ORDERS BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(
            @PathVariable Long userId) {

        List<OrderResponse> response =
                orderService.getOrdersByUser(userId);

        return ResponseEntity.ok(response);
    }

    // ORDER STATISTICS
    // IMPORTANT: This must come before /{id}
    @GetMapping("/statistics")
    public ResponseEntity<OrderStatisticsResponse> getOrderStatistics() {

        OrderStatisticsResponse response =
                orderService.getOrderStatistics();

        return ResponseEntity.ok(response);
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long id) {

        OrderResponse response =
                orderService.getOrder(id);

        return ResponseEntity.ok(response);
    }

    // CANCEL ORDER
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id) {

        OrderResponse response =
                orderService.cancelOrder(id);

        return ResponseEntity.ok(response);
    }
}