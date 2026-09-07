package com.rishav.commerce.order.api;

import com.rishav.commerce.order.domain.Order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

record OrderResponse(
    UUID id,
    UUID productId,
    int quantity,
    BigDecimal totalAmount,
    String status,
    Instant createdAt) {
  static OrderResponse from(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getProductId(),
        order.getQuantity(),
        order.getTotalAmount(),
        order.getStatus(),
        order.getCreatedAt());
  }
}
