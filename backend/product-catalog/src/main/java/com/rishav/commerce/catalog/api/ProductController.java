package com.rishav.commerce.catalog.api;

import com.rishav.commerce.catalog.domain.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
  private final ProductRepository repository;

  public ProductController(ProductRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<ProductResponse> all() {
    return repository.findByActiveTrueOrderByNameAsc().stream().map(ProductResponse::from).toList();
  }

  @GetMapping("/{id}")
  public ProductResponse one(@PathVariable UUID id) {
    return repository
        .findById(id)
        .map(ProductResponse::from)
        .orElseThrow(
            () ->
                new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product not found"));
  }
}
