package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.CategoryRequest;
import org.example.jpaentityrelationships.dto.CategoryResponse;
import org.example.jpaentityrelationships.entity.Category;
import org.example.jpaentityrelationships.exceptions.DuplicateResourceException;
import org.example.jpaentityrelationships.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // =========================================================
    // CREATE CATEGORY
    // =========================================================

    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse createCategory(
            CategoryRequest request
    ) {

        if (categoryRepository.existsByName(
                request.getName())) {

            throw new DuplicateResourceException(
                    "Category already exists"
            );
        }

        Category category = new Category();

        category.setName(request.getName());

        Category saved =
                categoryRepository.save(category);

        return toResponse(saved);
    }

    // =========================================================
    // GET ALL CATEGORIES
    // =========================================================

    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // CONVERT ENTITY TO RESPONSE
    // =========================================================

    private CategoryResponse toResponse(
            Category category
    ) {

        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}