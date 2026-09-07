package com.rishav.commerce.payment.domain;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "payments")
public class Payment {
  @Id UUID orderId;

  @Column(nullable = false)
  String paymentId;

  String razorpayOrderId;

  @Column(nullable = false)
  String status;

  protected Payment() {}

  public Payment(UUID id, String rzp) {
    orderId = id;
    razorpayOrderId = rzp;
    paymentId = "pending-" + id;
    status = "PENDING";
  }

  public UUID getOrderId() {
    return orderId;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public String getRazorpayOrderId() {
    return razorpayOrderId;
  }

  public String getStatus() {
    return status;
  }

  public void complete(String id) {
    paymentId = id;
    status = "PAID";
  }
}
