package com.rishav.commerce.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id private UUID id;
    private UUID productId;
    private int quantity;
    private BigDecimal totalAmount;
    private String status;
    private Instant createdAt;

    protected Order() { }

    public Order(UUID id, UUID productId, int quantity, BigDecimal totalAmount, Instant createdAt) {
        this.id = id; this.productId = productId; this.quantity = quantity;
        this.totalAmount = totalAmount; this.status = "PENDING_INVENTORY"; this.createdAt = createdAt;
    }

    public void markInventoryReserved() {
        this.status = "INVENTORY_RESERVED";
    }

    public void markOutOfStock() {
        this.status = "OUT_OF_STOCK";
    }
    public void markPaid() { this.status = "PAID"; }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
