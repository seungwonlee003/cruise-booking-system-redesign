package com.example.cruise_seat_reservation_system.exception;

public class DuplicateWebhookException extends RuntimeException{
    public DuplicateWebhookException(String message) {
        super(message);
    }
}
