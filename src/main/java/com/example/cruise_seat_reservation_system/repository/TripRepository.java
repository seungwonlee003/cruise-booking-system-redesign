package com.example.cruise_seat_reservation_system.repository;

import com.example.cruise_seat_reservation_system.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}
