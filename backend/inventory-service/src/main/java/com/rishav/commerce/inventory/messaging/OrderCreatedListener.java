package com.rishav.commerce.inventory.messaging;

import com.rishav.commerce.events.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class OrderCreatedListener {
    private final InventoryReservationService reservationService;
    OrderCreatedListener(InventoryReservationService reservationService) { this.reservationService = reservationService; }
    @KafkaListener(topics = "orders.created.v1", groupId = "inventory-service")
    void reserveStock(OrderCreatedEvent event) {
        reservationService.reserve(event);
        System.out.printf("Reserving %d item(s) for order %s%n", event.quantity(), event.orderId());
    }
}
