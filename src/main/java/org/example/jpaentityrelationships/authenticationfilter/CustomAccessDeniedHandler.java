package org.example.jpaentityrelationships.authenticationfilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.jpaentityrelationships.dto.ApiErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        ApiErrorResponse errorResponse =
                new ApiErrorResponse(
                        false,
                        "Access denied. You do not have permission to access this resource",
                        HttpStatus.FORBIDDEN.value(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json =
                "{"
                        + "\"success\":false,"
                        + "\"message\":\"Access denied. You do not have permission to access this resource\","
                        + "\"status\":403,"
                        + "\"path\":\"" + request.getRequestURI() + "\","
                        + "\"timestamp\":\"" + LocalDateTime.now() + "\""
                        + "}";

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}