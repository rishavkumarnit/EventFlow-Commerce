package com.rishav.commerce.order.messaging;

import com.rishav.commerce.events.InventoryReservedEvent;
import com.rishav.commerce.events.InventoryReservationFailedEvent;
import com.rishav.commerce.events.PaymentCompletedEvent;
import com.rishav.commerce.order.domain.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class InventoryReservedListener {
    private final OrderRepository orderRepository;

    InventoryReservedListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "inventory.reserved.v1", groupId = "order-service")
    @Transactional
    void markOrderAsReserved(InventoryReservedEvent event) {
        orderRepository.findById(event.orderId()).ifPresent(order -> order.markInventoryReserved());
    }

    @KafkaListener(topics = "inventory.reservation-failed.v1", groupId = "order-service")
    @Transactional
    void markOrderOutOfStock(InventoryReservationFailedEvent event) {
        orderRepository.findById(event.orderId()).ifPresent(order -> order.markOutOfStock());
    }
    @KafkaListener(topics = "payments.completed.v1", groupId = "order-service") @Transactional
    void markOrderPaid(PaymentCompletedEvent event) { orderRepository.findById(event.orderId()).ifPresent(order -> order.markPaid()); }
}
