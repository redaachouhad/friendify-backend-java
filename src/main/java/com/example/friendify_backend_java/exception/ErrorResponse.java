package com.example.friendify_backend_java.exception;

import com.example.friendify_backend_java.enums.ErrorCode;

import java.time.LocalDateTime;


// Unified Error Response
public record ErrorResponse(
        int status,
        String message,
        ErrorCode errorCode,
        Object details,
        LocalDateTime timestamp
) {
}
