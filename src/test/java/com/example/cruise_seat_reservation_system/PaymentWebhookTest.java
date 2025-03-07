package com.example.cruise_seat_reservation_system;

import com.example.cruise_seat_reservation_system.exception.DuplicateWebhookException;
import com.example.cruise_seat_reservation_system.exception.SeatReservationExpiredException;
import com.example.cruise_seat_reservation_system.model.*;
import com.example.cruise_seat_reservation_system.repository.SeatReservationRepository;
import com.example.cruise_seat_reservation_system.repository.ShipRepository;
import com.example.cruise_seat_reservation_system.repository.TripRepository;
import com.example.cruise_seat_reservation_system.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class PaymentWebhookTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private TripRepository tripRepository;

    private final Long userId = 123L;
    private final String paymentIntentId = "PAY_INTENT_ABC123";
    private Long seatId;

    @BeforeEach
    void setUp() {
        Ship ship = new Ship();
        shipRepository.save(ship);

        Trip trip = new Trip();
        trip.setShip(ship);
        tripRepository.save(trip);

        // Remove the local variable here
        SeatReservation seatReservation = new SeatReservation();
        seatReservation.setTrip(trip);
        seatReservation.setExpirationTime(LocalDateTime.now().plusMinutes(10));
        seatReservation.setReservedByUserId(userId);
        seatReservation.setReservationStatus(ReservationStatus.PENDING);
        seatReservation = seatReservationRepository.save(seatReservation);

        seatId = seatReservation.getSeatId();
    }

    @Test
    void testDuplicateSuccessWebhooks_areIdempotent() {
        Payment firstSuccess = new Payment();
        firstSuccess.setUserId(userId);
        firstSuccess.setSeatId(seatId);
        firstSuccess.setPaymentIntentId(paymentIntentId);

        paymentService.handleWebhook(firstSuccess);
        SeatReservation updated = seatReservationRepository.findById(seatId).get();

        Payment duplicateSuccess = new Payment();
        duplicateSuccess.setUserId(userId);
        duplicateSuccess.setSeatId(seatId);
        duplicateSuccess.setPaymentIntentId(paymentIntentId);
        assertThatThrownBy(() -> paymentService.handleWebhook(duplicateSuccess))
                .isInstanceOf(DuplicateWebhookException.class)
                .hasMessageContaining("Duplicate webhook");
    }

    @Test
    void testExpiredReservation_throwsException() {
        // Simulate a successful payment arriving after expiration
        Payment expiredPayment = new Payment();
        expiredPayment.setUserId(userId);
        expiredPayment.setSeatId(seatId);
        expiredPayment.setPaymentIntentId(paymentIntentId);
        expiredPayment.setPaymentDate(LocalDateTime.now().plusMinutes(20));

        // Expect SeatReservationExpiredException to be thrown
        assertThatThrownBy(() -> paymentService.handleWebhook(expiredPayment))
                .isInstanceOf(SeatReservationExpiredException.class)
                .hasMessageContaining("Seat Reservation expired (seatId=" + seatId + ")");
    }
}
