
# 🚢 Cruise Seat Reservation System 🎟️

## Overview
The **Cruise Seat Reservation System** is a booking backend designed for cruise ticket reservations. Inspired by my frustration with user inconveniences on the Ticket30 platform—such as booking delays and overbooking errors—I built this project to address those inefficiencies. It ensures fair seat allocation under moderate traffic loads and delivers error-resistant backend that eliminates common UX pain points through optimized design and reliable processing.

## Documentation
For a detailed breakdown of the concurrency handling and scalability features, check out my [Notion documentation](https://witty-neptune-851.notion.site/Concurrency-and-Scalability-in-Cruise-Ticket-Reservation-API-17fae38b9319803cb19ed4d749b076f2).

## Features
- **Two-Phase Booking**: Guarantees a first-come, first-served model to prevent race conditions.
- **High-Concurrency Handling**: Uses atomic operations to ensure consistency when multiple users attempt to reserve the same seat.
- **Idempotent webhook processing**: Ensures refunds in case of unexpected errors or failures.


## Tech Stack
- **Backend:** Spring Boot, JPA, Hibernate  
- **Database:** MySQL (with ACID-compliant transactions)  
- **Testing:** JUnit, Postman for API Testing  

## Project Structure
```bash
cruise-seat-reservation/
├── .mvn/                                # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/com/example/cruise_seat_reservation_system/
│   │   │   ├── controller/              # REST API endpoints (e.g., /trips, /seats/{seatId}/reserve)
│   │   │   ├── exception/               # Custom exception handling for robust error management
│   │   │   ├── model/                   # Entity classes (Ship, Trip, SeatReservation)
│   │   │   ├── repository/              # JPA repositories for database operations)
│   │   │   ├── service/                 # Business logic, including concurrency and webhook processing
│   │   │   └── CruiseSeatReservationSystemApplication.java
│   │   ├── resources/                   # Configuration files
│   ├── test/
│   │   ├── java/com/example/cruise_seat_reservation_system/
│   │   │   ├── CruiseSeatReservationSystemApplicationTests.java
│   │   │   ├── PaymentWebhookTest.java
│   │   │   └── SeatReservationServiceTest.java
├── .gitattributes                       # Git config
├── .gitignore                           # Git ignore rules
├── README.md                            # This file
├── mvnw                                 # Maven wrapper script (Unix)
├── mvnw.cmd                             # Maven wrapper script (Windows)
└── pom.xml                              # Maven dependencies
```
