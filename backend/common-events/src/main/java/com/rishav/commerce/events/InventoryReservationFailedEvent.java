package com.rishav.commerce.events;

import java.time.Instant;
import java.util.UUID;

public record InventoryReservationFailedEvent(
        UUID eventId,
        UUID orderId,
        UUID productId,
        int requestedQuantity,
        String reason,
        Instant occurredAt
) { }
