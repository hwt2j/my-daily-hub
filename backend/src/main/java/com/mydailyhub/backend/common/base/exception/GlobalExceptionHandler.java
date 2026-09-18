package com.mydailyhub.backend.common.base.exception;

import com.mydailyhub.backend.common.base.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        if (errorCode.getStatus().is5xxServerError()) {
            log.error("Business exception with server error status", exception);
        }
        String message = errorCode.getStatus().is5xxServerError()
                ? ErrorCode.INTERNAL_ERROR.getMessage() : exception.getMessage();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.failure(errorCode.name(), message));
    }

    // Covers MVC validation, malformed requests and ResponseStatusException.
    // Preserve framework status codes and headers, such as Allow for HTTP 405.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        ErrorCode errorCode = ErrorCode.fromStatus(statusCode);
        if (statusCode.is5xxServerError()) {
            log.error("MVC request processing failed", exception);
        }
        return super.handleExceptionInternal(exception,
                ApiResponse.failure(errorCode.name(), errorCode.getMessage()),
                headers, statusCode, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception exception) {
        log.error("Unexpected request processing failure", exception);
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.failure(errorCode.name(), errorCode.getMessage()));
    }
}
