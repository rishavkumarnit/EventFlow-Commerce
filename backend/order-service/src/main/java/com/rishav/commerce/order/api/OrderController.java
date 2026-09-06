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
import com.rishav.commerce.order.domain.Order;
import com.rishav.commerce.order.domain.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final OrderRepository orderRepository;

    OrderController(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate, OrderRepository orderRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    OrderCreatedEvent create(@Valid @RequestBody CreateOrderRequest request) {
        UUID orderId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        BigDecimal totalAmount = request.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));
        orderRepository.save(new Order(orderId, request.productId(), request.quantity(), totalAmount, createdAt));
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(), orderId, request.productId(), request.quantity(),
                totalAmount, createdAt);
        kafkaTemplate.send("orders.created.v1", orderId.toString(), event);
        return event;
    }
}
