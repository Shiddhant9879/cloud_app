package com.cloudapp.cloud_app.cloud_app.Repository;

import java.util.List;
import com.cloudapp.cloud_app.cloud_app.model.Payment.Payment;
import com.cloudapp.cloud_app.cloud_app.model.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
