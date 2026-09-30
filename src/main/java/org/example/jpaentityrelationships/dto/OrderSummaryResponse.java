package org.example.jpaentityrelationships.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummaryResponse {

    private Long orderId;
    private Long userId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;

    public OrderSummaryResponse(
            Long orderId,
            Long userId,
            String status,
            BigDecimal totalAmount,
            LocalDateTime createdAt,
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal price) {

        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}