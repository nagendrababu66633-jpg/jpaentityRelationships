
        package org.example.jpaentityrelationships.exceptions;

import jakarta.servlet.http.HttpServletRequest;

import org.example.jpaentityrelationships.dto.ApiErrorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authorization.AuthorizationDeniedException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // =========================================================
    // RESOURCE NOT FOUND - 404
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        log.warn(
                "Resource not found: {} - {} {}",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );

        ApiErrorResponse response = new ApiErrorResponse(
                false,
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    // =========================================================
    // DUPLICATE RESOURCE - 409
    // =========================================================

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(
            DuplicateResourceException exception,
            HttpServletRequest request) {

        log.warn(
                "Duplicate resource: {} - {} {}",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );

        ApiErrorResponse response = new ApiErrorResponse(
                false,
                exception.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }


    // =========================================================
    // BAD REQUEST - 400
    // =========================================================

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            BadRequestException exception,
            HttpServletRequest request) {

        log.warn(
                "Bad request: {} - {} {}",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );

        ApiErrorResponse response = new ApiErrorResponse(
                false,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // =========================================================
    // ACCESS DENIED - 403
    // =========================================================

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorizationDenied(
            AuthorizationDeniedException exception,
            HttpServletRequest request) {

        log.warn(
                "Access denied: {} {}",
                request.getMethod(),
                request.getRequestURI()
        );

        ApiErrorResponse response = new ApiErrorResponse(
                false,
                "Access denied. You do not have permission to access this resource",
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }


    // =========================================================
    // GENERAL EXCEPTION - 500
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(
            Exception exception,
            HttpServletRequest request) {

        log.error("========================================");
        log.error("UNEXPECTED ERROR");
        log.error("HTTP METHOD: {}", request.getMethod());
        log.error("REQUEST PATH: {}", request.getRequestURI());
        log.error(
                "EXCEPTION TYPE: {}",
                exception.getClass().getName()
        );
        log.error(
                "EXCEPTION MESSAGE: {}",
                exception.getMessage()
        );
        log.error("FULL STACK TRACE:", exception);
        log.error("========================================");


        // -----------------------------------------------------
        // TEMPORARY DEBUG RESPONSE
        // -----------------------------------------------------

        ApiErrorResponse response = new ApiErrorResponse(
                false,
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}