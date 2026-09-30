package org.example.jpaentityrelationships;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        exclude = {
                UserDetailsServiceAutoConfiguration.class
        }
)
@EnableCaching
@EnableAsync
@EnableScheduling
public class JpaEntityRealationshipsApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                JpaEntityRealationshipsApplication.class,
                args
        );
    }
}