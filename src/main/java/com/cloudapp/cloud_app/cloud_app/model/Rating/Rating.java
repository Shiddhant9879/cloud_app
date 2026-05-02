package com.cloudapp.cloud_app.cloud_app.model.Rating;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Rating {

    // separate primary key

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    // connected via join

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)

    private ServiceRequest serviceRequest;

    // rating value out of 5

    private int ratingValue;

    // created at

    private LocalDateTime createdAt;

}
