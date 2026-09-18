package com.mydailyhub.backend.common.base.exception;

import lombok.Getter;

import java.util.Objects;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(Objects.requireNonNull(errorCode).getMessage());
        this.errorCode = errorCode;
    }

    // The custom message is returned to the client; do not include internal details.
    public BusinessException(ErrorCode errorCode, String message) {
        super(Objects.requireNonNull(message));
        this.errorCode = Objects.requireNonNull(errorCode);
    }
}
