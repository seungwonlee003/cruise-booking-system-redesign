package com.example.cruise_seat_reservation_system.controller;

import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.model.Trip;
import com.example.cruise_seat_reservation_system.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips(){
        List<Trip> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long tripId){
        Trip trip = tripService.getTripById(tripId).orElseThrow(()->new RuntimeException("Trip not found"));
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{tripId}/seats")
    public ResponseEntity<List<SeatReservation>> getAllSeatsByTripId(@PathVariable Long tripId){
        List<SeatReservation> seatReservations = tripService.getAllSeatsByTripId(tripId);
        return ResponseEntity.ok(seatReservations);
    }

    // get all reservable seats by trip id
}
