package org.example.jpaentityrelationships.specification;

import org.example.jpaentityrelationships.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> nameContains(
            String name
    ) {

        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }


    public static Specification<Product> minPrice(
            BigDecimal minPrice
    ) {

        return (root, query, cb) -> {

            if (minPrice == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }


    public static Specification<Product> maxPrice(
            BigDecimal maxPrice
    ) {

        return (root, query, cb) -> {

            if (maxPrice == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }


    public static Specification<Product> categoryId(
            Long categoryId
    ) {

        return (root, query, cb) -> {

            if (categoryId == null) {
                return null;
            }

            return cb.equal(
                    root.get("category").get("id"),
                    categoryId
            );
        };
    }


    public static Specification<Product> active(
            Boolean active
    ) {

        return (root, query, cb) -> {

            if (active == null) {
                return null;
            }

            return cb.equal(
                    root.get("active"),
                    active
            );
        };
    }
}
