 package org.example.jpaentityrelationships.controller;

import org.example.jpaentityrelationships.dto.OrderItemRequest;
import org.example.jpaentityrelationships.dto.OrderItemResponse;
import org.example.jpaentityrelationships.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // CREATE ORDER ITEM
    @PostMapping("/order/{orderId}")
    public ResponseEntity<OrderItemResponse> createOrderItem(
            @PathVariable Long orderId,
            @RequestBody OrderItemRequest request) {

        OrderItemResponse response =
                orderItemService.createOrderItem(
                        orderId,
                        request
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET ORDER ITEM
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItem(
            @PathVariable Long id) {

        OrderItemResponse response =
                orderItemService.getOrderItem(id);

        return ResponseEntity.ok(response);
    }

    // DELETE ORDER ITEM
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long id) {

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.noContent().build();
    }
}
