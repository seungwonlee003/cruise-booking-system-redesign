package com.example.cruise_seat_reservation_system.service;

import com.example.cruise_seat_reservation_system.model.Payment;
import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.repository.SeatReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SeatReservationRepository seatReservationRepository;

    public void handleWebhook(Payment paymentPayload){
        Long userId = paymentPayload.getUserId();
        Long seatId = paymentPayload.getSeatId();
        String paymentIntentId = paymentPayload.getPaymentIntentId();

        SeatReservation seatReservation = seatReservationRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat Reservation Not Found"));

        if(seatReservation.getPaymentIntentId() != null){
            throw new RuntimeException("Seat Reservation has already been placed");
        }

        if(!Objects.equals(seatReservation.getReservedByUserId(), userId)){
            throw new RuntimeException("User is not reserved by the system");
        }

        if(seatReservation.getExpirationTime().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Seat Reservation has expired");
        }

        seatReservation.setReservedByUserId(userId);
        seatReservation.setPaymentIntentId(paymentIntentId);
    }
}
