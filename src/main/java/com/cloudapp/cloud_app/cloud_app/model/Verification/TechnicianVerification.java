package com.cloudapp.cloud_app.cloud_app.model.Verification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

@Entity
@Table(name = "technician_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TechnicianVerification {

    // unique key

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne

    @JoinColumn(name = "technician_id", nullable = false)

    private Technician technician;

    // document type (either Id or url link)

    @Column(nullable = false)
    private String documentUrl;

    // verification status (in enum)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private VerificationStatus verificationStatus;

    // the verification submission info
    @Column(nullable = false)
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;

}
