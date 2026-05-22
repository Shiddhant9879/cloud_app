package com.cloudapp.cloud_app.cloud_app.model.request;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

@Entity
@Table(name = "job_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which request this log belongs to
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    // who performed the action (nullable for system actions)
    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    // what happened
    @Enumerated(EnumType.STRING)
    private RequestStatus request;

    // when it happened
    private LocalDateTime timestamp;
}