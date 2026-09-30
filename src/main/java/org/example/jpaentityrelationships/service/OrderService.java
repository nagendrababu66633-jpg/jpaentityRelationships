package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.OrderItemRequest;
import org.example.jpaentityrelationships.dto.OrderItemResponse;
import org.example.jpaentityrelationships.dto.OrderRequest;
import org.example.jpaentityrelationships.dto.OrderResponse;
import org.example.jpaentityrelationships.dto.OrderStatisticsResponse;
import org.example.jpaentityrelationships.entity.Order;
import org.example.jpaentityrelationships.entity.OrderItem;
import org.example.jpaentityrelationships.entity.Product;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.exceptions.BadRequestException;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.OrderRepository;
import org.example.jpaentityrelationships.repository.ProductRepository;
import org.example.jpaentityrelationships.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // Added for today's Async task
    private final NotificationService notificationService;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            NotificationService notificationService) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;

        // Added for today's Async task
        this.notificationService = notificationService;
    }

    // =========================
    // CREATE ORDER
    // =========================
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new BadRequestException(
                    "Order must contain at least one item"
            );
        }

        User user = getLoggedInUser();

        Order order = new Order();
        order.setUser(user);
        order.setStatus("NEW");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            if (itemRequest.getQuantity() == null ||
                    itemRequest.getQuantity() <= 0) {

                throw new BadRequestException(
                        "Quantity must be greater than zero"
                );
            }

            Product product =
                    productRepository
                            .findById(itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found: "
                                                    + itemRequest.getProductId()
                                    )
                            );

            if (product.getStock() <
                    itemRequest.getQuantity()) {

                throw new BadRequestException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.addOrderItem(orderItem);

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            total = total.add(itemTotal);

            product.setStock(
                    product.getStock()
                            - itemRequest.getQuantity()
            );
        }

        order.setTotalAmount(total);

        Order savedOrder =
                orderRepository.save(order);

        // =====================================================
        // ASYNC ORDER NOTIFICATION
        // =====================================================

        notificationService.sendOrderNotification(
                savedOrder.getId(),
                user.getEmail()
        );

        return toResponse(savedOrder);
    }

    // =========================
    // GET MY ORDERS
    // =========================
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders() {

        User user = getLoggedInUser();

        return orderRepository
                .findOrdersWithItemsByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET ORDER BY ID
    // =========================
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {

        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found: " + id
                                )
                        );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority
                                        .getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        if (isAdmin) {
            return toResponse(order);
        }

        String email = authentication.getName();

        if (!order.getUser()
                .getEmail()
                .equals(email)) {

            throw new AccessDeniedException(
                    "You cannot access another user's order"
            );
        }

        return toResponse(order);
    }

    // =========================
    // GET ORDERS BY USER
    // =========================
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found: " + userId
            );
        }

        return orderRepository
                .findOrdersWithItemsByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // CANCEL ORDER
    // =========================
    @Transactional
    public OrderResponse cancelOrder(Long id) {

        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found: " + id
                                )
                        );

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        if (!order.getUser()
                .getEmail()
                .equals(email)) {

            throw new AccessDeniedException(
                    "You cannot cancel another user's order"
            );
        }

        if (!"NEW".equals(order.getStatus()) &&
                !"PENDING".equals(order.getStatus())) {

            throw new BadRequestException(
                    "Order cannot be cancelled in status: "
                            + order.getStatus()
            );
        }

        order.setStatus("CANCELLED");

        Order savedOrder =
                orderRepository.save(order);

        return toResponse(savedOrder);
    }

    // =========================
    // ORDER STATISTICS
    // =========================
    @Transactional(readOnly = true)
    public OrderStatisticsResponse getOrderStatistics() {

        return orderRepository.getOrderStatistics();
    }

    // =========================
    // GET LOGGED-IN USER
    // =========================
    private User getLoggedInUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in user not found"
                        )
                );
    }

    // =========================
    // CONVERT ORDER TO RESPONSE
    // =========================
    private OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

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