package com.example.cruise_seat_reservation_system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Duplicate webhooks: log and return 200 so
     * payment gateway won't keep retrying.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the error (at least a warning or error level)
        log.warn("Webhook exception caught: {}", ex.getMessage(), ex);

        // Always return 200 OK to avoid further retries
        return ResponseEntity.ok("Error handled, returning 200 to stop retries.");
    }
}
