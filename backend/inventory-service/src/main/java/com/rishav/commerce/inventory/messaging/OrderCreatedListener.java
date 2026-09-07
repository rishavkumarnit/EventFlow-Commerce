package com.rishav.commerce.inventory.messaging;

import com.rishav.commerce.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class OrderCreatedListener {
  private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);
  private final InventoryReservationService reservationService;

  OrderCreatedListener(InventoryReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @KafkaListener(topics = "orders.created.v1", groupId = "inventory-service")
  void reserveStock(OrderCreatedEvent event) {
    reservationService.reserve(event);
    log.info(
        "inventory.order_received orderId={} productId={} quantity={}",
        event.orderId(),
        event.productId(),
        event.quantity());
  }
}
