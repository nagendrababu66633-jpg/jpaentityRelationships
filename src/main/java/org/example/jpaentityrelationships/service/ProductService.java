 package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.ProductRequest;
import org.example.jpaentityrelationships.dto.ProductResponse;
import org.example.jpaentityrelationships.entity.Category;
import org.example.jpaentityrelationships.entity.Product;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.CategoryRepository;
import org.example.jpaentityrelationships.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    // CREATE PRODUCT
    public ProductResponse createProduct(ProductRequest request) {

        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found: " +
                                                request.getCategoryId()));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setActive(request.getActive());
        product.setCategory(category);

        Product savedProduct =
                productRepository.save(product);

        return toResponse(savedProduct);
    }


    // GET PRODUCT BY ID
    public ProductResponse getProduct(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found: " + id));

        return toResponse(product);
    }


    // GET ALL PRODUCTS
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // UPDATE PRODUCT
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found: " + id));

        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found: " +
                                                request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setActive(request.getActive());
        product.setCategory(category);

        Product updatedProduct =
                productRepository.save(product);

        return toResponse(updatedProduct);
    }


    // DELETE PRODUCT
    public void deleteProduct(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found: " + id));

        productRepository.delete(product);
    }


    // CONVERT ENTITY TO RESPONSE
    private ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getActive(),
                product.getCategory().getId()
        );
    }
}
