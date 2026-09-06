package com.rishav.commerce.order.api;

import com.rishav.commerce.events.OrderCreatedEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import com.rishav.commerce.order.domain.Order;
import com.rishav.commerce.order.domain.OrderRepository;
import com.rishav.commerce.order.outbox.OrderOutboxService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderRepository orderRepository;
    private final OrderOutboxService outboxService;

    OrderController(OrderRepository orderRepository, OrderOutboxService outboxService) {
        this.orderRepository = orderRepository; this.outboxService = outboxService;
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
        outboxService.enqueue(event);
        log.info("order.created orderId={} productId={} quantity={} totalAmount={}", orderId, request.productId(), request.quantity(), totalAmount);
        return event;
    }

    @GetMapping("/{orderId}")
    OrderResponse findById(@PathVariable UUID orderId) {
        return orderRepository.findById(orderId)
                .map(OrderResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @GetMapping
    List<OrderResponse> recent() {
        return orderRepository.findTop10ByOrderByCreatedAtDesc().stream().map(OrderResponse::from).toList();
    }
}
