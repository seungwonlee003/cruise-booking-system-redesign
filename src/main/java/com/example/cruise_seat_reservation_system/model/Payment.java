package com.example.cruise_seat_reservation_system.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private String paymentIntentId;
    private Long userId;
    private Long seatId;
    private LocalDateTime paymentDate;
}
