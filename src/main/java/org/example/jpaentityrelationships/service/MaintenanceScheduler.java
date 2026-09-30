package org.example.jpaentityrelationships.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class MaintenanceScheduler {

    @Scheduled(fixedRate = 300000)
    public void performMaintenance() {

        log.info(
                "Scheduled maintenance started at {}",
                LocalDateTime.now()
        );

        // Maintenance logic

        log.info(
                "Scheduled maintenance completed at {}",
                LocalDateTime.now()
        );
    }
}