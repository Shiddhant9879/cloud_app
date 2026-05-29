package com.cloudapp.cloud_app.cloud_app.Service;

import com.cloudapp.cloud_app.cloud_app.model.Payment.Payment;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service

public class PaymentService {

    // repositories

    private final ServiceRequestRepository servicerequestRepository;

    public PaymentService(ServiceRequestRepository servicerequestRepository) {

        this.servicerequestRepository = servicerequestRepository;
    }

    // Create payment record for service request

    public Payment createPayment(ServiceRequest request, BigDecimal amount) {

        Payment payment = new Payment();

        payment.setAmount(amount);
        payment.setCreatedAt(LocalDateTime.now());
        request.setPayment(payment);

        return servicerequestRepository.save(request).getPayment();
    }

}
