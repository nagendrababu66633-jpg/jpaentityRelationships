package org.example.jpaentityrelationships.repository;


import org.example.jpaentityrelationships.entity.TopCustomerResponse;
import org.example.jpaentityrelationships.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT new org.example.jpaentityrelationships.entity.TopCustomerResponse(
                u.id,
                u.firstName,
                u.lastName,
                u.email,
                SUM(o.totalAmount)
            )
            FROM User u
            JOIN Order o ON o.user = u
            GROUP BY u.id, u.firstName, u.lastName, u.email
            ORDER BY SUM(o.totalAmount) DESC
            """)
    List<TopCustomerResponse> findTopCustomers();
}