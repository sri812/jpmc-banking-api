package com.cbdg.interview.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle TransactionsNotFoundException
    @ExceptionHandler(TransactionsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionsNotFound(TransactionsNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("errorCode", ex.getErrorCode());
        response.put("timestamp", ex.getTimestamp());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle AccountNotFoundException
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFound(AccountNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("errorCode", ex.getErrorCode());
        response.put("timestamp", ex.getTimestamp());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle generic exceptions (for unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("errorCode", "INTERNAL_SERVER_ERROR");
        response.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
