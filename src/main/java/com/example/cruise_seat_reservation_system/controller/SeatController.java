package com.example.cruise_seat_reservation_system.controller;

import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/{seatId}")
    public ResponseEntity<SeatReservation> getSeat(@PathVariable Long seatId){
        SeatReservation seatReservation = seatService.getSeat(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));
        return ResponseEntity.ok(seatReservation);
    }

    @PostMapping("/{seatId}/reserve")
    public ResponseEntity<SeatReservation> reserveSeat(@PathVariable Long seatId, @RequestBody Long userId){
        SeatReservation seatReservation = seatService.reserveSeat(seatId, userId);
        return ResponseEntity.ok(seatReservation);
    }
}
