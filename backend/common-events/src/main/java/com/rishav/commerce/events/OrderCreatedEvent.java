package com.rishav.commerce.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Immutable event contract shared by the order and inventory bounded contexts. */
public record OrderCreatedEvent(
    UUID eventId,
    UUID orderId,
    UUID productId,
    int quantity,
    BigDecimal totalAmount,
    Instant occurredAt) {}
