package org.example.jpaentityrelationships.repository;


import org.example.jpaentityrelationships.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String Number);
}