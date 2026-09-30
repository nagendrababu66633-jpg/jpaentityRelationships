package org.example.jpaentityrelationships.service;

import lombok.RequiredArgsConstructor;
import org.example.jpaentityrelationships.entity.Product;
import org.example.jpaentityrelationships.exceptions.DuplicateResourceException;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.ProductRepository;
import org.example.jpaentityrelationships.specification.ProductSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found: " + id
                        ));
    }

    // =========================================================
    // GET PRODUCTS WITH PAGINATION
    // =========================================================

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#pageable")
    public Page<Product> getProducts(Pageable pageable) {

        return productRepository.findAll(pageable);
    }

    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {

        if (productRepository.existsByNameIgnoreCase(
                product.getName())) {

            throw new DuplicateResourceException(
                    "Product already exists: "
                            + product.getName()
            );
        }

        return productRepository.save(product);
    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(
            Long id,
            Product updatedProduct) {

        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(
                updatedProduct.getDescription()
        );
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        return productRepository.save(existingProduct);
    }

    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {

        Product product = getProductById(id);

        productRepository.delete(product);
    }

    // =========================================================
    // SEARCH PRODUCTS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(
            String name,
            String description,
            Double minPrice,
            Double maxPrice,
            boolean onlyAvailable,
            Pageable pageable) {

        // Start with an empty specification.
        // Specification.where() is deprecated in Spring Data 3.5+
        Specification<Product> specification =
                (root, query, criteriaBuilder) -> null;

        specification = specification.and(
                ProductSpecification.hasName(name)
        );

        specification = specification.and(
                ProductSpecification.hasDescription(description)
        );

        specification = specification.and(
                ProductSpecification
                        .priceGreaterThanOrEqualTo(minPrice)
        );

        specification = specification.and(
                ProductSpecification
                        .priceLessThanOrEqualTo(maxPrice)
        );

        if (onlyAvailable) {
            specification = specification.and(
                    ProductSpecification.hasStock()
            );
        }

        return productRepository.findAll(
                specification,
                pageable
        );
    }
}