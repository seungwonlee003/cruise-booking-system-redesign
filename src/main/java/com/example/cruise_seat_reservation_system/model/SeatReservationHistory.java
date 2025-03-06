package com.example.cruise_seat_reservation_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReservationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatReservation seatReservation;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false)
    private ReservationStatus oldStatus;
    // can be ANY of AVAILABLE, PENDING, PAID, WAITING_FOR_REFUND, REFUNDED

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private ReservationStatus newStatus;

    private Long changedByUserId;

    private LocalDateTime changedTimestamp;

    private String reason; // optional
}
