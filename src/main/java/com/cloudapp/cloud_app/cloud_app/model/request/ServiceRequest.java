package com.cloudapp.cloud_app.cloud_app.model.request;

import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;

import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

import java.time.LocalDateTime;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ServiceRequests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ServiceRequest {

    // id

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // customer who made the request

    @ManyToOne

    @JoinColumn(name = "customer_id")

    private Customer customer;

    // description of the service request

    private String description;

    // technician assigned to the request(nullable until assigned)

    @ManyToOne

    @JoinColumn(name = "technician_id", nullable = true)

    @MapsId

    private Technician technician;

    // status of the request(e.g., "pending", "in_progress", "completed")

    @Enumerated(EnumType.STRING)
    private RequestStatus request;

    // timestamp of when the request was created

    private LocalDateTime createdAt;

    // timestamap of when the request was Last updated

    private LocalDateTime updatedAt;

}
