package com.example.cruise_seat_reservation_system.repository;

import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.model.Trip;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.seatId = :id")
    Optional<SeatReservation> findByIdWithPessimisticLock(@Param("id") Long id);

    List<SeatReservation> findAllByTrip(Trip trip);

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.trip = :trip " +
            "AND (sr.expirationTime IS NULL OR sr.expirationTime < :currentTime)")
    List<SeatReservation> findAllAvailableSeatsByTrip(@Param("trip") Trip trip,
                                                      @Param("currentTime") LocalDateTime currentTime);
}
