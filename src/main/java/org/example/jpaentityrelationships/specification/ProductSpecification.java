package org.example.jpaentityrelationships.specification;

import org.example.jpaentityrelationships.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {

            if (name == null || name.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase().trim() + "%"
            );
        };
    }

    public static Specification<Product> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {

            if (description == null || description.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + description.toLowerCase().trim() + "%"
            );
        };
    }

    public static Specification<Product> priceGreaterThanOrEqualTo(
            Double minPrice) {

        return (root, query, criteriaBuilder) -> {

            if (minPrice == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }

    public static Specification<Product> priceLessThanOrEqualTo(
            Double maxPrice) {

        return (root, query, criteriaBuilder) -> {

            if (maxPrice == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }

    public static Specification<Product> hasStock() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(
                        root.get("stock"),
                        0
                );
    }
}