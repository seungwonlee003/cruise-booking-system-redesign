package com.example.cruise_seat_reservation_system.exception;

public class SeatReservationExpiredException extends RuntimeException{
    public SeatReservationExpiredException(String message){
        super(message);
    }
}
