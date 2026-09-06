package com.rishav.commerce.payment.messaging;

import com.rishav.commerce.events.InventoryReservedEvent;
import com.rishav.commerce.events.PaymentCompletedEvent;
import com.rishav.commerce.payment.domain.Payment;
import com.rishav.commerce.payment.domain.PaymentRepository;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentProcessor {
    private static final Logger log = LoggerFactory.getLogger(PaymentProcessor.class);
    private final PaymentRepository repo;
    private final KafkaTemplate<String, Object> kafka;
    private final RazorpayService razorpay;

    PaymentProcessor(PaymentRepository repo, KafkaTemplate<String, Object> kafka, RazorpayService razorpay) { this.repo = repo; this.kafka = kafka; this.razorpay = razorpay; }

    @KafkaListener(topics = "inventory.reserved.v1", groupId = "payment-service")
    @Transactional
    void pay(InventoryReservedEvent event) {
        if (repo.existsById(event.orderId())) return;
        Payment payment = repo.save(new Payment(event.orderId(), razorpay.create(event.orderId(), event.totalAmount())));
        log.info("payment.awaiting_razorpay_checkout orderId={}", event.orderId());
    }

    @Transactional
    public void verify(UUID orderId, String razorpayOrderId, String razorpayPaymentId, String signature) {
        Payment payment = repo.findById(orderId).orElseThrow();
        if (!payment.getRazorpayOrderId().equals(razorpayOrderId) || !razorpay.valid(razorpayOrderId, razorpayPaymentId, signature)) throw new IllegalArgumentException("Invalid Razorpay signature");
        if (!"PAID".equals(payment.getStatus())) { payment.complete(razorpayPaymentId); kafka.send("payments.completed.v1", orderId.toString(), new PaymentCompletedEvent(UUID.randomUUID(), orderId, razorpayPaymentId, Instant.now())); }
    }
}
