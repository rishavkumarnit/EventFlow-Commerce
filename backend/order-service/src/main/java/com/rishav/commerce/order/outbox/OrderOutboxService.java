package com.rishav.commerce.order.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishav.commerce.events.OrderCreatedEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderOutboxService {
  private final OutboxEventRepository outboxEvents;
  private final ObjectMapper objectMapper;

  OrderOutboxService(OutboxEventRepository outboxEvents, ObjectMapper objectMapper) {
    this.outboxEvents = outboxEvents;
    this.objectMapper = objectMapper;
  }

  public void enqueue(OrderCreatedEvent event) {
    try {
      outboxEvents.save(
          new OutboxEvent(
              UUID.randomUUID(),
              event.orderId(),
              "orders.created.v1",
              event.getClass().getName(),
              objectMapper.writeValueAsString(event),
              Instant.now()));
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException("Could not serialize order event for outbox", exception);
    }
  }
}
