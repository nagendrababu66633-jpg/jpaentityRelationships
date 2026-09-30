
        package org.example.jpaentityrelationships.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.jpaentityrelationships.dto.ApiResponse;
import org.example.jpaentityrelationships.dto.PageResponse;
import org.example.jpaentityrelationships.entity.Product;
import org.example.jpaentityrelationships.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(
        name = "Product API V1",
        description = "Product management APIs"
)
public class ProductController {

    private final ProductService productService;


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    @Operation(
            summary = "Get product by ID",
            description = "Returns a product using its ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProduct(
            @PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product retrieved successfully",
                        product
                )
        );
    }


    // =========================================================
    // GET ALL PRODUCTS - PAGINATION
    // =========================================================

    @Operation(
            summary = "Get all products",
            description = "Returns paginated products with sorting"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Product>>>
    getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        int safePage = Math.max(page, 0);

        int safeSize = Math.min(Math.max(size, 1), 100);

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        safePage,
                        safeSize,
                        Sort.by(sortDirection, sortBy)
                );

        Page<Product> productPage =
                productService.getProducts(pageable);

        PageResponse<Product> response =
                PageResponse.from(productPage);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products retrieved successfully",
                        response
                )
        );
    }


    // =========================================================
    // SEARCH PRODUCTS
    // =========================================================

    @Operation(
            summary = "Search products",
            description = "Search products using name, description, price and availability"
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<Product>>>
    searchProducts(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            Double minPrice,

            @RequestParam(required = false)
            Double maxPrice,

            @RequestParam(defaultValue = "false")
            boolean onlyAvailable,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction) {

        int safePage = Math.max(page, 0);

        int safeSize = Math.min(Math.max(size, 1), 100);

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        safePage,
                        safeSize,
                        Sort.by(sortDirection, sortBy)
                );

        Page<Product> productPage =
                productService.searchProducts(
                        name,
                        description,
                        minPrice,
                        maxPrice,
                        onlyAvailable,
                        pageable
                );

        PageResponse<Product> response =
                PageResponse.from(productPage);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products searched successfully",
                        response
                )
        );
    }


    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    @Operation(
            summary = "Create product",
            description = "Creates a new product. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Product>>
    createProduct(@RequestBody Product product) {

        Product savedProduct =
                productService.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Product created successfully",
                                savedProduct
                        )
                );
    }


    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    @Operation(
            summary = "Update product",
            description = "Updates an existing product. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>>
    updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        Product updatedProduct =
                productService.updateProduct(id, product);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product updated successfully",
                        updatedProduct
                )
        );
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @Operation(
            summary = "Delete product",
            description = "Deletes a product. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>
    deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product deleted successfully",
                        null
                )
        );
    }


    // =========================================================
    // ADMIN ONLY - 403 TEST
    // =========================================================

    @Operation(
            summary = "Admin access test",
            description = "Test endpoint available only for ADMIN users"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-test")
    public ResponseEntity<ApiResponse<String>> adminTest() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin access successful",
                        "Only ADMIN users can access this endpoint"
                )
        );
    }
}

