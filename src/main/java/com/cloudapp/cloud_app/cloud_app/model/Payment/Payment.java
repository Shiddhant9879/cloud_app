package com.cloudapp.cloud_app.cloud_app.model.Payment;

import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id", nullable = false)
    private ServiceRequest serviceRequest;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentstatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}