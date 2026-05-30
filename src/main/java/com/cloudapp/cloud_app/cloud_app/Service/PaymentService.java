package com.cloudapp.cloud_app.cloud_app.Service;

import com.cloudapp.cloud_app.cloud_app.model.Payment.Payment;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.PaymentRepository;
import com.cloudapp.cloud_app.cloud_app.model.Payment.PaymentStatus;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import com.cloudapp.cloud_app.cloud_app.Repository.CustomerRepository;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service

public class PaymentService {

    // repositories

    public final PaymentRepository paymentRepository;
    public final ServiceRequestRepository serviceRequestRepository;

    // constructor

    public PaymentService(PaymentRepository paymentRepository, ServiceRequestRepository serviceRequestRepository) {

        this.paymentRepository = paymentRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    // create payment

    public Payment createPayment(Long serviceRequestId, BigDecimal amount) {

        ServiceRequest request = serviceRequestRepository.findById(serviceRequestId)
                .orElseThrow(() -> new RuntimeException("Service Request not found"));

        // create payment method

        Payment payment = new Payment();

        payment.setAmount(amount);
        payment.setServiceRequest(request);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setPaymentstatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);

    }

    // workflow after payment completion

    public Payment completePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentstatus(PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    // happy flow for payment failure

    public Payment failPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentstatus(PaymentStatus.FAILED);

        return paymentRepository.save(payment);
    }
}
