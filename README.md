# Cruise Seat Reservation System 

## Introduction
This is a backend-side improvement of the original service (app): "가보고 싶은 섬" (Ticket30).
While not being a fully fledged service, it addresses the server-side specific pain points the author has experienced
from the original platform and explores concurrency and payment webhook idempotence.

## Features
1. Faster-Payment-Wins: Uses a two-phase booking design with a fencing token to prevent a later seat acquirer from winning by completing payment faster.
2. Booking Consistency: Resolves concurrency issues from relational database transactional isolation levels using atomic queries.
3. Webhook Idempotency: Ensures idempotency in payment webhooks, handling network partition scenarios when contacting third-party APIs.

## Article
I wrote two articles to reflect on the troubleshooting details:

Article 1: 

Article 2:
