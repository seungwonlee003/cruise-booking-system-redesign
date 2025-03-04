package com.example.cruise_seat_reservation_system.service;

import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.model.Trip;
import com.example.cruise_seat_reservation_system.repository.SeatReservationRepository;
import com.example.cruise_seat_reservation_system.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final SeatReservationRepository seatReservationRepository;

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long tripId) {
        return tripRepository.findById(tripId);
    }

    public List<SeatReservation> getAllSeatsByTripId(Long tripId) {
        Trip trip = getTripById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        return seatReservationRepository.findAllByTrip(trip);
    }
}
