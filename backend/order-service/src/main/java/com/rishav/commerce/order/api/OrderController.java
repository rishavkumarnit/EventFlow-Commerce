package com.rishav.commerce.order.api;

import com.rishav.commerce.events.OrderCreatedEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    OrderController(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    OrderCreatedEvent create(@Valid @RequestBody CreateOrderRequest request) {
        UUID orderId = UUID.randomUUID();
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(), orderId, request.productId(), request.quantity(),
                request.unitPrice().multiply(BigDecimal.valueOf(request.quantity())), Instant.now());
        kafkaTemplate.send("orders.created.v1", orderId.toString(), event);
        return event;
    }
}
