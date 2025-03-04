package com.example.cruise_seat_reservation_system.service;

import com.example.cruise_seat_reservation_system.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    public void handleWebhook(Payment paymentPayload){
        Long userId = paymentPayload.getUserId();
        Long seatId = paymentPayload.getSeatId();
    }
}
