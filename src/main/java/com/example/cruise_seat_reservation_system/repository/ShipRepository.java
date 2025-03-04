package com.example.cruise_seat_reservation_system.repository;

import com.example.cruise_seat_reservation_system.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
}
