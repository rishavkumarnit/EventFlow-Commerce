package com.rishav.commerce.inventory.messaging;

import com.rishav.commerce.events.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class OrderCreatedListener {
    @KafkaListener(topics = "orders.created.v1", groupId = "inventory-service")
    void reserveStock(OrderCreatedEvent event) {
        // Next slice: persist an idempotency key, reserve stock, then publish inventory.reserved.v1.
        System.out.printf("Reserving %d item(s) for order %s%n", event.quantity(), event.orderId());
    }
}
