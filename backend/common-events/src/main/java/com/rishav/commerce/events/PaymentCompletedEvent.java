package com.rishav.commerce.events;
import java.time.Instant; import java.util.UUID;
public record PaymentCompletedEvent(UUID eventId, UUID orderId, String paymentId, Instant occurredAt) { }
