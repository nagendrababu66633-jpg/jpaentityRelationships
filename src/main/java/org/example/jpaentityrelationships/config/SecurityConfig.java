package org.example.jpaentityrelationships.config;

import org.example.jpaentityrelationships.authenticationfilter.CustomAccessDeniedHandler;
import org.example.jpaentityrelationships.authenticationfilter.CustomAuthenticationEntryPoint;
import org.example.jpaentityrelationships.authenticationfilter.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // =================================================
                // CSRF
                // =================================================

                .csrf(csrf ->
                        csrf.disable()
                )

                // =================================================
                // STATELESS SESSION
                // =================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =================================================
                // 401 AND 403 HANDLERS
                // =================================================

                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(
                                        new CustomAuthenticationEntryPoint()
                                )
                                .accessDeniedHandler(
                                        new CustomAccessDeniedHandler()
                                )
                )

                // =================================================
                // AUTHORIZATION
                // =================================================

                .authorizeHttpRequests(auth ->

                        auth

                                // =================================================
                                // LOGIN / REGISTER
                                // =================================================

                                .requestMatchers(
                                        "/api/auth/**"
                                )
                                .permitAll()

                                // =================================================
                                // SWAGGER
                                // =================================================

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                )
                                .permitAll()

                                // =================================================
                                // ACTUATOR HEALTH
                                // PUBLIC
                                // =================================================

                                .requestMatchers(
                                        "/actuator/health",
                                        "/actuator/health/**"
                                )
                                .permitAll()

                                // =================================================
                                // ACTUATOR INFO
                                // PUBLIC
                                // =================================================

                                .requestMatchers(
                                        "/actuator/info"
                                )
                                .permitAll()

                                // =================================================
                                // ACTUATOR CACHES
                                // PUBLIC
                                // =================================================

                                .requestMatchers(
                                        "/actuator/caches",
                                        "/actuator/caches/**"
                                )
                                .permitAll()

                                // =================================================
                                // ACTUATOR SCHEDULED TASKS
                                // PUBLIC
                                // =================================================

                                .requestMatchers(
                                        "/actuator/scheduledtasks",
                                        "/actuator/scheduledtasks/**"
                                )
                                .permitAll()

                                // =================================================
                                // ACTUATOR METRICS
                                // ADMIN ONLY
                                // =================================================

                                .requestMatchers(
                                        "/actuator/metrics",
                                        "/actuator/metrics/**"
                                )
                                .hasRole("ADMIN")

                                // =================================================
                                // ALL OTHER APIs
                                // LOGIN REQUIRED
                                // =================================================

                                .anyRequest()
                                .authenticated()
                )

                // =================================================
                // JWT AUTHENTICATION FILTER
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}