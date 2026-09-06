package com.rishav.commerce.payment.api;
import com.rishav.commerce.payment.domain.*;import com.rishav.commerce.payment.messaging.*;import java.util.*;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/payments") public class PaymentController { final PaymentRepository repo; final RazorpayService razorpay; final PaymentProcessor processor; public PaymentController(PaymentRepository r,RazorpayService z,PaymentProcessor p){repo=r;razorpay=z;processor=p;}
@GetMapping("/{orderId}/checkout") public Checkout checkout(@PathVariable UUID orderId){var p=repo.findById(orderId).orElseThrow();return new Checkout(razorpay.key(),p.getRazorpayOrderId());}
@PostMapping("/{orderId}/verify") public void verify(@PathVariable UUID orderId,@RequestBody Verify body){processor.verify(orderId,body.razorpayOrderId(),body.razorpayPaymentId(),body.razorpaySignature());}
public record Checkout(String keyId,String razorpayOrderId){} public record Verify(String razorpayOrderId,String razorpayPaymentId,String razorpaySignature){}
}
