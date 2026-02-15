package com.example.friendify_backend_java.exception;

import com.example.friendify_backend_java.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public BusinessException(ErrorCode errorCode,
                             HttpStatus status,
                             String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
