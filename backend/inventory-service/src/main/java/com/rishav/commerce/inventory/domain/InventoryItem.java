package com.rishav.commerce.inventory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {
    @Id private UUID productId;
    private int availableQuantity;
    private int reservedQuantity;

    protected InventoryItem() { }
    public InventoryItem(UUID productId, int availableQuantity) { this.productId = productId; this.availableQuantity = availableQuantity; }
    public void reserve(int quantity) {
        if (availableQuantity < quantity) throw new IllegalStateException("Insufficient stock for product " + productId);
        availableQuantity -= quantity;
        reservedQuantity += quantity;
    }
}
