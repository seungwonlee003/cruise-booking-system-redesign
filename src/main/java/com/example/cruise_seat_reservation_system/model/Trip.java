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
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    private String startLocation;

    private String endLocation;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private LocalDateTime ticketSaleStartDate;

    private LocalDateTime ticketSaleEndDate;
}