package com.cloudapp.cloud_app.cloud_app.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.cloudapp.cloud_app.cloud_app.Service.PaymentService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudapp.cloud_app.cloud_app.Dto.PaymentRequestDto;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Phase 1 - Create Payment

    @PostMapping("/create/{requestId}")
    public String createPayment(
            @PathVariable Long requestId,
            @RequestBody PaymentRequestDto paymentRequest) {

        paymentService.createPayment(
                requestId,
                paymentRequest.getAmount());

        return "Payment request initiated";
    }

    // Phase 2 - Payment Success

    @PostMapping("/success/payment/{paymentId}")
    public String paymentSuccess(
            @PathVariable Long paymentId) {

        paymentService.completePayment(paymentId);

        return "Payment completed successfully";
    }

    // Phase 3 - Payment Failure

    @PostMapping("/failure/payment/{paymentId}")
    public String paymentFailure(
            @PathVariable Long paymentId) {

        paymentService.failPayment(paymentId);

        return "Payment failed. Please retry.";
    }
}