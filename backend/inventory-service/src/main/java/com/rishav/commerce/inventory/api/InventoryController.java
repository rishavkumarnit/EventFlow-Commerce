package com.rishav.commerce.inventory.api;

import com.rishav.commerce.inventory.domain.InventoryItemRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/inventory")
@org.springframework.web.bind.annotation.CrossOrigin(
    origins = "${FRONTEND_ORIGIN:http://localhost:5173}")
class InventoryController {
  private final InventoryItemRepository inventory;

  InventoryController(InventoryItemRepository inventory) {
    this.inventory = inventory;
  }

  @GetMapping("/{productId}")
  InventoryResponse findByProductId(@PathVariable UUID productId) {
    return inventory
        .findById(productId)
        .map(InventoryResponse::from)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
  }
}
