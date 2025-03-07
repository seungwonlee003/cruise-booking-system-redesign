package com.example.cruise_seat_reservation_system.controller;

import com.example.cruise_seat_reservation_system.model.Payment;
import com.example.cruise_seat_reservation_system.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/webhooks")
    public ResponseEntity<String> handleWebhook(@RequestBody Payment paymentPayload){
        paymentService.handleWebhook(paymentPayload);
        return ResponseEntity.ok("Payment processed successfully");
    }
}
