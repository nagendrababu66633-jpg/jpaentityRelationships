package org.example.jpaentityrelationships.repository;

import org.example.jpaentityrelationships.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category,Long> {
    boolean existsByName(String email);
}
