
        package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.OrderItemRequest;
import org.example.jpaentityrelationships.dto.OrderItemResponse;
import org.example.jpaentityrelationships.entity.Order;
import org.example.jpaentityrelationships.entity.OrderItem;
import org.example.jpaentityrelationships.entity.Product;
import org.example.jpaentityrelationships.exceptions.BadRequestException;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.OrderItemRepository;
import org.example.jpaentityrelationships.repository.OrderRepository;
import org.example.jpaentityrelationships.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // CREATE ORDER ITEM
    @Transactional
    public OrderItemResponse createOrderItem(
            Long orderId,
            OrderItemRequest request
    ) {

        // Check order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );

        // Check product
        Product product = productRepository.findById(
                request.getProductId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Product not found with id: "
                                + request.getProductId()
                )
        );

        // Check quantity
        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {

            throw new BadRequestException(
                    "Quantity must be greater than zero"
            );
        }

        // Check stock
        if (product.getStock() < request.getQuantity()) {

            throw new BadRequestException(
                    "Insufficient stock for product: "
                            + product.getName()
            );
        }

        // Create OrderItem
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(request.getQuantity());

        // Save current product price
        orderItem.setPrice(product.getPrice());

        // Reduce stock
        product.setStock(
                product.getStock() - request.getQuantity()
        );

        Product savedProduct =
                productRepository.save(product);

        OrderItem savedOrderItem =
                orderItemRepository.save(orderItem);

        return toResponse(savedOrderItem);
    }


    // GET ORDER ITEM
    public OrderItemResponse getOrderItem(Long id) {

        OrderItem orderItem =
                orderItemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order item not found with id: "
                                                + id
                                )
                        );

        return toResponse(orderItem);
    }


    // DELETE ORDER ITEM
    @Transactional
    public void deleteOrderItem(Long id) {

        OrderItem orderItem =
                orderItemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order item not found with id: "
                                                + id
                                )
                        );

        orderItemRepository.delete(orderItem);
    }


    // ENTITY → RESPONSE
    private OrderItemResponse toResponse(
            OrderItem orderItem
    ) {

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}

