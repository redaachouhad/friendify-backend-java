package com.example.friendify_backend_java.exception;

import com.example.friendify_backend_java.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===============================
    // DTO VALIDATION ERRORS
    // ===============================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage())
                );

        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> errors.put("confirmPassword", error.getDefaultMessage()));


        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                ErrorCode.VALIDATION_ERROR,
                errors,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    // ===============================
    // JSON PARSE / ENUM ERRORS
    // ===============================
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ErrorResponse> handleJsonParse(
//            HttpMessageNotReadableException ex
//    ) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        if(ex.getCause() instanceof InvalidFormatException invalidFormatEx){
//            String fieldName = invalidFormatEx.getPath().getFirst().getPropertyName();
//            if ("gender".equals(fieldName)) {
//                errors.put("gender", "Please select a gender");
//            }
//        }
//
//
//        log.warn("Invalid request format", ex);
//
//        ErrorResponse response = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Invalid request format",
//                ErrorCode.INVALID_REQUEST_FORMAT,
//                errors,
//                LocalDateTime.now()
//        );
//
//        return ResponseEntity.badRequest().body(response);
//    }

    // ===============================
    // BUSINESS EXCEPTIONS
    // ===============================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex
    ) {

        log.warn("Business exception: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                ex.getStatus().value(),
                ex.getMessage(),
                ex.getErrorCode(),
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    // ===============================
    // DATABASE ERRORS
    // ===============================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDatabase(
            DataIntegrityViolationException ex
    ) {

        log.error("Database error", ex);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Database constraint violation",
                ErrorCode.DATABASE_ERROR,
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // ===============================
    // GLOBAL FALLBACK
    // ===============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex) {

        log.error("Unhandled exception", ex);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                ErrorCode.INTERNAL_SERVER_ERROR,
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}