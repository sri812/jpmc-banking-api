package com.cbdg.interview.exception;

import java.time.LocalDateTime;

public class TransactionsNotFoundException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime timestamp;

    public TransactionsNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TransactionsNotFoundException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
