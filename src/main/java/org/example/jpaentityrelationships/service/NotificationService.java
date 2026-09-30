package org.example.jpaentityrelationships.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class NotificationService {

    @Async("taskExecutor")
    public CompletableFuture<Void> sendOrderNotification(
            Long orderId,
            String email) {

        log.info(
                "Starting async order notification. Order ID: {}, Email: {}",
                orderId,
                email
        );

        try {

            // Simulate notification processing
            Thread.sleep(3000);

            log.info(
                    "Order notification sent successfully. Order ID: {}, Email: {}",
                    orderId,
                    email
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            log.error(
                    "Order notification interrupted. Order ID: {}",
                    orderId,
                    e
            );

            return CompletableFuture.failedFuture(e);
        }

        return CompletableFuture.completedFuture(null);
    }
}