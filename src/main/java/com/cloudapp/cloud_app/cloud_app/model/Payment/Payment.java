package com.cloudapp.cloud_app.cloud_app.model.Payment;

import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Payment {

    // separte primary key for payment request id

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    // connected from the requestservice

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)

    private ServiceRequest serviceRequest;

    // amount to be paid

    private BigDecimal amount;

    // payment status (in enumerated value here the status selection would be from
    // the enum class)
    @Enumerated(EnumType.STRING)

    private PaymentStatus paymentstatus;

    // timestamps of the payment

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
