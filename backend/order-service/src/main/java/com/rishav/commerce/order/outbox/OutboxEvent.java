package com.rishav.commerce.order.outbox;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
class OutboxEvent {
  @Id private UUID id;
  private UUID aggregateId;
  private String topic;
  private String eventType;
  private String payload;
  private Instant createdAt;
  private Instant publishedAt;

  protected OutboxEvent() {}

  OutboxEvent(
      UUID id,
      UUID aggregateId,
      String topic,
      String eventType,
      String payload,
      Instant createdAt) {
    this.id = id;
    this.aggregateId = aggregateId;
    this.topic = topic;
    this.eventType = eventType;
    this.payload = payload;
    this.createdAt = createdAt;
  }

  UUID getAggregateId() {
    return aggregateId;
  }

  String getTopic() {
    return topic;
  }

  String getPayload() {
    return payload;
  }

  void markPublished() {
    this.publishedAt = Instant.now();
  }
}
