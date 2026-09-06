package com.rishav.commerce.payment.api;

import com.rishav.commerce.payment.domain.PaymentRepository;
import com.rishav.commerce.payment.messaging.PaymentProcessor;
import com.rishav.commerce.payment.messaging.RazorpayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Razorpay Test Mode checkout and server-side signature verification")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    private final PaymentRepository repository;
    private final RazorpayService razorpay;
    private final PaymentProcessor processor;

    public PaymentController(PaymentRepository repository, RazorpayService razorpay, PaymentProcessor processor) {
        this.repository = repository;
        this.razorpay = razorpay;
        this.processor = processor;
    }

    @GetMapping("/{orderId}/checkout")
    @Operation(summary = "Get Razorpay checkout details", description = "Returns the public Test Mode key and Razorpay order ID after inventory is reserved.")
    @ApiResponse(responseCode = "200", description = "Checkout details returned")
    @ApiResponse(responseCode = "404", description = "No payment checkout exists for this order")
    public Checkout checkout(@PathVariable UUID orderId) {
        var payment = repository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment checkout not found for this order"));
        return new Checkout(razorpay.key(), payment.getRazorpayOrderId());
    }

    @PostMapping("/{orderId}/verify")
    @Operation(summary = "Verify Razorpay payment", description = "Validates Razorpay's signature and emits the payment-completed Kafka event.")
    @ApiResponse(responseCode = "200", description = "Payment verified")
    @ApiResponse(responseCode = "400", description = "Payment signature or Razorpay order ID is invalid")
    public void verify(@PathVariable UUID orderId, @RequestBody Verify body) {
        processor.verify(orderId, body.razorpayOrderId(), body.razorpayPaymentId(), body.razorpaySignature());
    }

    public record Checkout(
            @Schema(description = "Razorpay Test Mode public key", example = "rzp_test_********") String keyId,
            @Schema(description = "Razorpay order ID used by Checkout", example = "order_********") String razorpayOrderId) {}

    public record Verify(
            @Schema(description = "Razorpay order ID") String razorpayOrderId,
            @Schema(description = "Razorpay payment ID") String razorpayPaymentId,
            @Schema(description = "HMAC signature returned by Razorpay Checkout") String razorpaySignature) {}
}
