package com.example.friendify_backend_java.security;

import com.example.friendify_backend_java.enums.ErrorCode;
import com.example.friendify_backend_java.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class is triggered whenever an AuthenticationException occurs.
 *
 * It handles:
 * - Bad credentials (wrong email/password)
 * - Expired JWT
 * - Invalid JWT
 * - Missing token
 * - Disabled account
 *
 * It converts authentication exceptions into structured JSON responses.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * This method is automatically called by Spring Security
     * when authentication fails.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        // Default response values
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = "Unauthorized";
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        // The original cause of the exception (useful for JWT errors)
        Throwable cause = authException.getCause();

        /*
         * 1️⃣ Wrong email or password
         * Thrown by AuthenticationManager during login.
         */
        if (authException instanceof BadCredentialsException) {
            message = "Invalid email or password";
            errorCode = ErrorCode.INVALID_CREDENTIALS;
        }

        /*
         * 2️⃣ Account disabled
         * Happens if user.isEnabled() == false
         */
        else if (authException instanceof DisabledException) {
            message = "Account is disabled";
            errorCode = ErrorCode.ACCOUNT_DISABLED;
        }

        /*
         * 3️⃣ JWT expired
         * Triggered when token expiration time has passed.
         */
        else if (cause instanceof ExpiredJwtException) {
            message = "JWT token expired";
            errorCode = ErrorCode.JWT_EXPIRED;
        }

        /*
         * 4️⃣ Invalid or malformed JWT
         * Signature invalid, token corrupted, etc.
         */
        else if (cause instanceof JwtException) {
            message = "Invalid JWT token";
            errorCode = ErrorCode.INVALID_TOKEN;
        }

        /*
         * 5️⃣ Missing authentication (no token provided)
         */
        else if (authException.getMessage() != null &&
                authException.getMessage().contains("Full authentication is required")) {

            message = "Authentication required";
            errorCode = ErrorCode.AUTHENTICATION_REQUIRED;
        }

        // Build structured error response
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                errorCode,
                null,
                LocalDateTime.now()
        );

        // Configure HTTP response
        response.setContentType("application/json");
        response.setStatus(status.value());

        // Write JSON body to output stream
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
