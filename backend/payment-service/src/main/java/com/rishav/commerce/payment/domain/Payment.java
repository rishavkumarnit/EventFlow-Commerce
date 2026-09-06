package com.rishav.commerce.payment.domain; import jakarta.persistence.*; import java.util.*;
@Entity @Table(name="payments") public class Payment { @Id UUID orderId; @Column(nullable=false) String paymentId; protected Payment(){} public Payment(UUID orderId){this.orderId=orderId;this.paymentId="pay_"+UUID.randomUUID().toString().replace("-","");} public String getPaymentId(){return paymentId;} }
