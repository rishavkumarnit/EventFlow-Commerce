package com.rishav.commerce.catalog.api;

import com.rishav.commerce.catalog.domain.Product;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id, String sku, String name, BigDecimal price, boolean active) {
  static ProductResponse from(Product p) {
    return new ProductResponse(p.getId(), p.getSku(), p.getName(), p.getPrice(), p.isActive());
  }
}
