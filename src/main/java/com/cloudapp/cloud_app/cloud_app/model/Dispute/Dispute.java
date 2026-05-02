package com.cloudapp.cloud_app.cloud_app.model.Dispute;

import jakarta.persistence.*;
import lombok.*;

import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

import java.time.LocalDateTime;

@Entity
@Table(name = "dispute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Dispute {

    // separate primary key

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // connected via service request class

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)

    private ServiceRequest serviceRequest;

    // dispute issue description
    @Column(nullable = false)
    private String reason;
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)

    private DisputeStatus disputestatus;

    // creation time of the dispute

    private LocalDateTime createdAt;

    // resolution time of the conflict

    private LocalDateTime resolvedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.disputestatus = DisputeStatus.In_review;
    }

}
