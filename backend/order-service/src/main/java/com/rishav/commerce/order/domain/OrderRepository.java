package com.rishav.commerce.order.domain;

import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findTop10ByOrderByCreatedAtDesc();
}
