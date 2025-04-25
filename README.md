# Cruise Seat Reservation System 

## Introduction
This is a backend-side improvement of the original service (app): "가보고 싶은 섬" (Ticket30).
While not being a fully fledged service, it addresses the server-side specific pain points the author has experienced from the original platform and explores tricky edge cases such as concurrency in booking and payment.

## Features
1. Faster-Payment-Wins: Uses a two-phase booking design with a fencing token to prevent a later seat acquirer from winning by completing payment faster.
2. Booking Consistency: Resolves concurrency issues from relational database transactional isolation levels using atomic queries.
3. Webhook Idempotency: Ensures idempotency in payment webhooks, handling network partition scenarios when contacting third-party APIs.

## Endpoints

### Trip Endpoints:
```http
GET /trips – Get all trips with basic info and seat availability.
GET /trips/{tripId} – Get full details for a specific trip.
GET /trips/{tripId}/seats – See which seats are available on a trip.
```
### Seat Endpoints
```http
GET /seats/{seatId} – Get info about a specific seat.
POST /seats/{seatId}/reserve – Reserve a specific seat.
```
### Payment Endpoint
```http
POST /payments/webhooks - Handle payment status updates from your payment provider.
```

## Article
If you are interested in the troubleshooting details:

Article: 
