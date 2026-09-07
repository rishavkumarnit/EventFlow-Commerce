package com.rishav.commerce.order.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderRequest(
    @NotNull UUID productId, @Positive int quantity, @NotNull @Min(0) BigDecimal unitPrice) {}
