package org.example.jpaentityrelationships.entity;

import ch.qos.logback.core.status.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime createdAt = LocalDateTime.now();
    private BigDecimal totalAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // FIX 1: Make sure this field is List<OrderItem>
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getStatus() { return status; }
    public void setStatus(String status ){
        this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime  createdAt) { this.createdAt = createdAt; }


    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal  TotalAmount) { this.totalAmount = totalAmount; }
    // FIX 2: Return type must be List<OrderItem>
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    // FIX 3: Parameter type must be List<OrderItem>
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    public void addOrderItem(OrderItem item){
        orderItems.add(item);
        item.setOrder(this);}
    public void removeOrderItem(OrderItem item){
        orderItems.remove(item);
        item.setOrder(null);
    }
}