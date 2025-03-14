
# 🚢 Cruise Seat Reservation System 🎟️

## Overview
The **Cruise Seat Reservation System** is a **highly scalable**, **concurrency-optimized** booking backend designed for cruise ticket reservations.  
It ensures **fair seat allocation** while handling **high traffic loads efficiently** and optimizes the booking process to eliminate user experience issues found in the original Ticket30 service.

## Features
✅ **Two-Phase Booking**: Guarantees a **first-come, first-served** model to prevent race conditions.  
✅ **High-Concurrency Handling**: Uses **atomic operations** to ensure consistency when multiple users attempt to reserve the same seat.  
✅ **Idempotent webhook processing**: Ensures refunds in case of unexpected errors or failures.


## 🛠️ Tech Stack
- **Backend:** Spring Boot, JPA, Hibernate  
- **Database:** MySQL (with ACID-compliant transactions)  
- **Testing:** JUnit, Postman for API Testing  

