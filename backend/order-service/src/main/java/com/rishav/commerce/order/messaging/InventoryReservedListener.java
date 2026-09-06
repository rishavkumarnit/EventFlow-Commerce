package com.rishav.commerce.order.messaging;

import com.rishav.commerce.events.InventoryReservedEvent;
import com.rishav.commerce.events.InventoryReservationFailedEvent;
import com.rishav.commerce.events.PaymentCompletedEvent;
import com.rishav.commerce.order.domain.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
class InventoryReservedListener {
    private static final Logger log = LoggerFactory.getLogger(InventoryReservedListener.class);
    private final OrderRepository orderRepository;

    InventoryReservedListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "inventory.reserved.v1", groupId = "order-service")
    @Transactional
    void markOrderAsReserved(InventoryReservedEvent event) {
        orderRepository.findById(event.orderId()).ifPresent(order -> order.markInventoryReserved());
        log.info("order.inventory_reserved orderId={} quantity={}", event.orderId(), event.quantity());
    }

    @KafkaListener(topics = "inventory.reservation-failed.v1", groupId = "order-service")
    @Transactional
    void markOrderOutOfStock(InventoryReservationFailedEvent event) {
        orderRepository.findById(event.orderId()).ifPresent(order -> order.markOutOfStock());
        log.warn("order.out_of_stock orderId={} reason={}", event.orderId(), event.reason());
    }
    @KafkaListener(topics = "payments.completed.v1", groupId = "order-service") @Transactional
    void markOrderPaid(PaymentCompletedEvent event) { orderRepository.findById(event.orderId()).ifPresent(order -> order.markPaid()); log.info("order.paid orderId={} paymentId={}", event.orderId(), event.paymentId()); }
}
