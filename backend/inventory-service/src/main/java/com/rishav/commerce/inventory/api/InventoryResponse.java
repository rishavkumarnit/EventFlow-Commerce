package com.rishav.commerce.inventory.api;

import com.rishav.commerce.inventory.domain.InventoryItem;
import java.util.UUID;

record InventoryResponse(UUID productId, int availableQuantity, int reservedQuantity) {
    static InventoryResponse from(InventoryItem item) {
        return new InventoryResponse(item.getProductId(), item.getAvailableQuantity(), item.getReservedQuantity());
    }
}
