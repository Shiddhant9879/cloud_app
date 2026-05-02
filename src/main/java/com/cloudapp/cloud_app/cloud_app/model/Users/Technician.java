package com.cloudapp.cloud_app.cloud_app.model.Users;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "technicians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Technician {

    // user id

    @Id
    private Long id;

    // linking it to the user table

    @OneToOne

    @MapsId

    @JoinColumn(name = "user_id", referencedColumnName = "id")

    private Users user;

    // verification status of the technician

    private boolean isVerified;

    // technician's availability status
    @Enumerated(EnumType.STRING)

    public AvailabilityStatus status;

    // technician's rating

    private double rating;
}