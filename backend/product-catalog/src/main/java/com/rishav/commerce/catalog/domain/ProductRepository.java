package com.rishav.commerce.catalog.domain;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ProductRepository extends JpaRepository<Product, UUID> { List<Product> findByActiveTrueOrderByNameAsc(); }
