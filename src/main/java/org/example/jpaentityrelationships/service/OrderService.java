 package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.OrderItemRequest;
import org.example.jpaentityrelationships.dto.OrderItemResponse;
import org.example.jpaentityrelationships.dto.OrderRequest;
import org.example.jpaentityrelationships.dto.OrderResponse;
import org.example.jpaentityrelationships.entity.Order;
import org.example.jpaentityrelationships.entity.OrderItem;
import org.example.jpaentityrelationships.entity.Product;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.exceptions.BadRequestException;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.OrderRepository;
import org.example.jpaentityrelationships.repository.ProductRepository;
import org.example.jpaentityrelationships.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    // CREATE ORDER
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new BadRequestException(
                    "Order must contain at least one item");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " +
                                        request.getUserId()));

        Order order = new Order();

        order.setUser(user);
        order.setStatus("NEW");

        BigDecimal total = BigDecimal.ZERO;


        for (OrderItemRequest itemRequest :
                request.getItems()) {

            // Validate quantity
            if (itemRequest.getQuantity() == null ||
                    itemRequest.getQuantity() <= 0) {

                throw new BadRequestException(
                        "Quantity must be greater than zero");
            }


            // Find product
            Product product =
                    productRepository.findById(
                                    itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found: " +
                                                    itemRequest.getProductId()));


            // Check stock
            if (product.getStock() <
                    itemRequest.getQuantity()) {

                throw new BadRequestException(
                        "Insufficient stock for product: " +
                                product.getName());
            }


            // Create OrderItem
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);

            orderItem.setQuantity(
                    itemRequest.getQuantity());

            orderItem.setPrice(
                    product.getPrice());


            // Add item to order
            order.addOrderItem(orderItem);


            // Calculate item total
            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()));


            total = total.add(itemTotal);


            // Reduce stock
            product.setStock(
                    product.getStock() -
                            itemRequest.getQuantity());
        }


        // Set total amount
        order.setTotalAmount(total);


        // Save order
        Order savedOrder =
                orderRepository.save(order);


        return toResponse(savedOrder);
    }


    // GET ORDER BY ID
    public OrderResponse getOrder(Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found: " + id));

        return toResponse(order);
    }


    // GET ORDERS BY USER
    public List<OrderResponse> getOrdersByUser(
            Long userId) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found: " + userId);
        }

        return orderRepository
                .findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // CONVERT ENTITY TO RESPONSE
    private OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                new ArrayList<>();


        for (OrderItem item :
                order.getOrderItems()) {

            OrderItemResponse itemResponse =
                    new OrderItemResponse(
                            item.getId(),
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getPrice()
                    );

            items.add(itemResponse);
        }


        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}
