
        package org.example.jpaentityrelationships.repository;

import org.example.jpaentityrelationships.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    // =========================================================
    // FIND PRODUCT BY ID
    // =========================================================

    @Override
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findById(Long id);


    // =========================================================
    // FIND ALL PRODUCTS - PAGINATION
    // =========================================================

    @Override
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAll(Pageable pageable);


    // =========================================================
    // SEARCH PRODUCTS - SPECIFICATION + PAGINATION
    // =========================================================

    @Override
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAll(
            Specification<Product> specification,
            Pageable pageable
    );


    // =========================================================
    // CHECK DUPLICATE PRODUCT
    // =========================================================

    boolean existsByNameIgnoreCase(String name);
}
