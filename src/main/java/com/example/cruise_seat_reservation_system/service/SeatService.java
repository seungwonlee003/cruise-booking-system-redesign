package com.example.cruise_seat_reservation_system.service;

import com.example.cruise_seat_reservation_system.model.ReservationStatus;
import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.repository.SeatReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatReservationRepository seatReservationRepository;

    public Optional<SeatReservation> getSeat(Long seatId) {
        return seatReservationRepository.findById(seatId);
    }

    @Transactional
    public SeatReservation reserveSeat(Long seatId, Long userId){
        SeatReservation seatReservation = seatReservationRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat Not Found"));

        if(seatReservation.getExpirationTime() != null && seatReservation.getExpirationTime().isAfter(LocalDateTime.now())) throw new RuntimeException("Another user has reserved it already");

        seatReservation.setReservationStatus(ReservationStatus.PENDING);
        seatReservation.setReservedByUserId(userId);
        seatReservation.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        return seatReservation;
    }
}
