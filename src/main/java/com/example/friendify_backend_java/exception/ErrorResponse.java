package com.example.friendify_backend_java.exception;

import java.time.LocalDateTime;


// Unified Error Response
public record ErrorResponse(
        int status,
        String message,
        Object details,
        LocalDateTime timestamp
) {
}
