package org.example.jpaentityrelationships.repository;

import org.example.jpaentityrelationships.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}