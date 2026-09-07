package com.rishav.commerce.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InventoryReservedEvent(
    UUID eventId,
    UUID orderId,
    UUID productId,
    int quantity,
    BigDecimal totalAmount,
    Instant occurredAt) {}
