package org.example.jpaentityrelationships.repository;

import org.example.jpaentityrelationships.dto.OrderStatisticsResponse;
import org.example.jpaentityrelationships.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // GET ORDERS BY USER
    List<Order> findByUserId(Long userId);

    // N+1 FIX - GET ORDERS WITH ORDER ITEMS
    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.orderItems
            WHERE o.user.id = :userId
            """)
    List<Order> findOrdersWithItemsByUserId(
            @Param("userId") Long userId
    );

    // ORDER STATISTICS
    @Query("""
            SELECT new org.example.jpaentityrelationships.dto.OrderStatisticsResponse(
                COUNT(o),
                SUM(o.totalAmount),
                AVG(o.totalAmount)
            )
            FROM Order o
            """)
    OrderStatisticsResponse getOrderStatistics();
}