package com.example.cruise_seat_reservation_system.service;

import com.example.cruise_seat_reservation_system.exception.DuplicateWebhookException;
import com.example.cruise_seat_reservation_system.exception.SeatReservationExpiredException;
import com.example.cruise_seat_reservation_system.model.Payment;
import com.example.cruise_seat_reservation_system.model.ReservationStatus;
import com.example.cruise_seat_reservation_system.model.SeatReservation;
import com.example.cruise_seat_reservation_system.repository.SeatReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final SeatReservationRepository seatReservationRepository;

    @Transactional
    public void handleWebhook(Payment paymentPayload) {
        Long userId = paymentPayload.getUserId();
        Long seatId = paymentPayload.getSeatId();
        String incomingPaymentIntentId = paymentPayload.getPaymentIntentId();

        // for-update clause
        SeatReservation seatReservation = seatReservationRepository
                .findByIdWithPessimisticLock(seatId)
                .orElseThrow(() -> {
                    log.error("event=seat_not_found seat_id={} user_id={} message=Seat reservation not found",
                            seatId, userId);
                    return new RuntimeException("Seat Reservation Not Found for seatId=" + seatId);
                });

        // 1) Idempotence check
        if (seatReservation.getPaymentIntentId() != null) {
            if (Objects.equals(seatReservation.getPaymentIntentId(), incomingPaymentIntentId)) {
                // Same Payment Intent -> definitely a duplicate request
                log.warn("event=duplicate_webhook seat_id={} user_id={} payment_intent_id={} " +
                                "message=This payment is already processed for this seat",
                        seatId, userId, incomingPaymentIntentId);

                throw new DuplicateWebhookException("Duplicate webhook: payment already processed (seatId=" + seatId + ")");
            } else {
                // A different Payment Intent means the seat was paid with some other ID
                log.warn("event=payment_mismatch seat_id={} user_id={} existing_payment_intent={} new_incoming_payment_intent={} " +
                                "message=Seat is already paid with a different ID",
                        seatId, userId, seatReservation.getPaymentIntentId(), incomingPaymentIntentId);

                throw new DuplicateWebhookException("Duplicate webhook: seat is already paid with a different ID (seatId=" + seatId + ")");
            }
        }

        // 2) If seatReservation is not paid yet, ensure user matches the reserved user
        if (!Objects.equals(seatReservation.getReservedByUserId(), userId)) {
            log.error("event=user_mismatch seat_id={} user_id={} reserved_by_user_id={} message=User mismatch for this seat",
                    seatId, userId, seatReservation.getReservedByUserId());

            throw new RuntimeException("User mismatch for seatId=" + seatId);
        }

        // 3) Check for expired reservation
        if (seatReservation.getExpirationTime().isBefore(paymentPayload.getPaymentDate())) {
            log.error("event=reservation_expired_requires_refund seat_id={} user_id={} expiration_time={} message=Seat reservation has expired, refund required",
                    seatId, userId, seatReservation.getExpirationTime());

            throw new SeatReservationExpiredException("Seat Reservation expired (seatId=" + seatId + ")");
        }

        log.info("event=webhook_success seat_id={} user_id={} payment_intent_id={} message=Webhook handled successfully",
                seatId, userId, incomingPaymentIntentId);

        seatReservation.setPaymentIntentId(incomingPaymentIntentId);  // Store the payment intent
        seatReservation.setReservationStatus(ReservationStatus.PAID);
        seatReservation.setReservedTime(LocalDateTime.now());
    }

}
