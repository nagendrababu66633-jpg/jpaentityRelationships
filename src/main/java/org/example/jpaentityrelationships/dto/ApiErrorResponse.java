package org.example.jpaentityrelationships.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private boolean success;

    private String message;

    private int status;

    private String path;

    private LocalDateTime timestamp;
}