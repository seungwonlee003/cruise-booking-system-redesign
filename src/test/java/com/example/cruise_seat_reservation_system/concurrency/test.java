package com.example.cruise_seat_reservation_system.concurrency;

import com.example.cruise_seat_reservation_system.model.ReservationStatus;
import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.model.Ship;
import com.example.cruise_seat_reservation_system.model.Trip;
import com.example.cruise_seat_reservation_system.repository.SeatReservationRepository;
import com.example.cruise_seat_reservation_system.repository.ShipRepository;
import com.example.cruise_seat_reservation_system.repository.TripRepository;
import com.example.cruise_seat_reservation_system.service.SeatService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class test {

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private TripRepository tripRepository;

    private Long seatId;

    @BeforeAll
    public void beforeAll(){
        Ship ship = new Ship();
        shipRepository.save(ship);

        Trip trip = new Trip();
        trip.setShip(ship);
        tripRepository.save(trip);

        SeatReservation seatReservation = new SeatReservation();
        seatReservation.setTrip(trip);
        seatReservation.setReservationStatus(ReservationStatus.AVAILABLE);
        SeatReservation savedSeatReservation = seatReservationRepository.save(seatReservation);
        seatId = savedSeatReservation.getSeatId();
    }

    @Test
    public void sequential_test() {
        int userCount = 20;
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        for (int i = 0; i < userCount; i++) {
            try {
                seatService.reserveSeat(seatId, (long) (i + 1));
                successCount.incrementAndGet();
            } catch (Exception e) {
                failureCount.incrementAndGet();
            }
        }
        System.out.println("Success: " + successCount.get());
        System.out.println("Failure: " + failureCount.get());

        // then
        assertThat(successCount.get()).isEqualTo(1);
    }
}
