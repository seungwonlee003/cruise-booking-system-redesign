package com.example.cruise_seat_reservation_system.repository;

import com.example.cruise_seat_reservation_system.model.SeatReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReservationHistoryRepository extends JpaRepository<SeatReservationHistory, Long> {
}
