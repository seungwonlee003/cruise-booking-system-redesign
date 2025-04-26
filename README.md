# Cruise Booking System Redesign

## Introduction
This project is a backend-focused improvement of the original service, “가보고 싶은 섬” (Ticket30).

While not a fully-fledged production service, it aims to address specific server-side pain points the author encountered in the original platform. It also explores complex scenarios such as concurrency issues in booking and payment flows.

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
