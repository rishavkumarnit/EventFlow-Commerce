package com.rishav.commerce.order.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishav.commerce.events.OrderCreatedEvent;
import java.util.concurrent.ExecutionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class OutboxPublisher {
  private final OutboxEventRepository outboxEvents;
  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

  OutboxPublisher(
      OutboxEventRepository outboxEvents,
      ObjectMapper objectMapper,
      KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
    this.outboxEvents = outboxEvents;
    this.objectMapper = objectMapper;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Scheduled(fixedDelayString = "${app.outbox.publish-interval-ms:1000}")
  @Transactional
  void publishPendingEvents() {
    for (OutboxEvent event : outboxEvents.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc()) {
      try {
        OrderCreatedEvent payload =
            objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class);
        kafkaTemplate.send(event.getTopic(), event.getAggregateId().toString(), payload).get();
        event.markPublished();
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Could not publish outbox event", exception);
      } catch (JsonProcessingException | ExecutionException exception) {
        throw new IllegalStateException("Could not publish outbox event", exception);
      }
    }
  }
}
