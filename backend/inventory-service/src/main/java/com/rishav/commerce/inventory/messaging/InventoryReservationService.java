package com.rishav.commerce.inventory.messaging;

import com.rishav.commerce.events.InventoryReservedEvent;
import com.rishav.commerce.events.InventoryReservationFailedEvent;
import com.rishav.commerce.events.OrderCreatedEvent;
import com.rishav.commerce.inventory.domain.InventoryItemRepository;
import com.rishav.commerce.inventory.domain.ProcessedEvent;
import com.rishav.commerce.inventory.domain.ProcessedEventRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
class InventoryReservationService {
    private static final Logger log = LoggerFactory.getLogger(InventoryReservationService.class);
    private final ProcessedEventRepository processedEvents;
    private final InventoryItemRepository inventory;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    InventoryReservationService(ProcessedEventRepository processedEvents, InventoryItemRepository inventory,
                                KafkaTemplate<String, Object> kafkaTemplate) {
        this.processedEvents = processedEvents; this.inventory = inventory; this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    void reserve(OrderCreatedEvent event) {
        if (processedEvents.existsById(event.eventId())) return;
        try {
            var item = inventory.findById(event.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown product " + event.productId()));
            item.reserve(event.quantity());
            processedEvents.save(new ProcessedEvent(event.eventId()));
            kafkaTemplate.send("inventory.reserved.v1", event.orderId().toString(), new InventoryReservedEvent(
                    UUID.randomUUID(), event.orderId(), event.productId(), event.quantity(), event.totalAmount(), Instant.now()));
            log.info("inventory.reserved orderId={} productId={} quantity={}", event.orderId(), event.productId(), event.quantity());
        } catch (IllegalArgumentException | IllegalStateException exception) {
            processedEvents.save(new ProcessedEvent(event.eventId()));
            kafkaTemplate.send("inventory.reservation-failed.v1", event.orderId().toString(),
                    new InventoryReservationFailedEvent(UUID.randomUUID(), event.orderId(), event.productId(),
                            event.quantity(), exception.getMessage(), Instant.now()));
            log.warn("inventory.reservation_failed orderId={} reason={}", event.orderId(), exception.getMessage());
        }
    }
}
