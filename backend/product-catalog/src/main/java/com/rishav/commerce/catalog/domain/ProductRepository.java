package com.rishav.commerce.catalog.domain;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  List<Product> findByActiveTrueOrderByNameAsc();
}
