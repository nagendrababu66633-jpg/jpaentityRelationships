package org.example.jpaentityrelationships.repository;

import org.example.jpaentityrelationships.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
