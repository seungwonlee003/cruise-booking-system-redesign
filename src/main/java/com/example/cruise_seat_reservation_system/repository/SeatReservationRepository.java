package com.example.cruise_seat_reservation_system.repository;

import com.example.cruise_seat_reservation_system.model.ReservationStatus;
import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.model.Trip;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("UPDATE SeatReservation sr " +
            "SET sr.reservationStatus = :newStatus, " +
            "    sr.reservedByUserId = :userId, " +
            "    sr.expirationTime = :expirationTime " +
            "WHERE sr.seatId = :seatId " +
            "  AND (sr.expirationTime IS NULL OR sr.expirationTime < :currentTime)")
    int updateSeatReservationAtomically(@Param("seatId") Long seatId,
                                        @Param("userId") Long userId,
                                        @Param("newStatus") ReservationStatus newStatus,
                                        @Param("expirationTime") LocalDateTime expirationTime,
                                        @Param("currentTime") LocalDateTime currentTime);
}
