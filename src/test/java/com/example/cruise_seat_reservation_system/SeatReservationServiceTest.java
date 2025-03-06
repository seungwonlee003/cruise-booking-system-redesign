package com.example.cruise_seat_reservation_system;

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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeatReservationServiceTest {

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
    void beforeAll(){
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
    void shouldReserveSeatSequentiallyWithOnlyOneSuccess() {
        final int userCount = 20;
        final AtomicInteger successCount = new AtomicInteger();
        final AtomicInteger failureCount = new AtomicInteger();

        for (int i = 0; i < userCount; i++) {
            final long userId = i + 1;
            try {
                seatService.reserveSeat(seatId, userId);
                successCount.incrementAndGet();
            } catch (Exception ex) {
                failureCount.incrementAndGet();
            }
        }

        System.out.println("Success: " + successCount.get());
        System.out.println("Failure: " + failureCount.get());

        assertThat(successCount.get()).isEqualTo(1);
    }

    @Test
    void shouldReserveSeatConcurrentlyWithOnlyOneSuccess() throws InterruptedException {
        final int userCount = 20;
        final ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        final CountDownLatch countDownLatch = new CountDownLatch(userCount);

        final AtomicInteger successCount = new AtomicInteger();
        final AtomicInteger failureCount = new AtomicInteger();

        // Start timing
        long startTime = System.nanoTime();

        for (int i = 0; i < userCount; i++) {
            final long userId = i + 1;
            executorService.submit(() -> {
                try {
                    seatService.reserveSeat(seatId, userId);
                    successCount.incrementAndGet();
                } catch (Exception ex) {
                    failureCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // Stop timing
        long endTime = System.nanoTime();
        long elapsedNanos = endTime - startTime;
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(elapsedNanos);

        System.out.println("Success: " + successCount.get());
        System.out.println("Failure: " + failureCount.get());
        System.out.println("Elapsed Time: " + elapsedMillis + " ms");

        assertThat(successCount.get()).isEqualTo(1);
    }

}
