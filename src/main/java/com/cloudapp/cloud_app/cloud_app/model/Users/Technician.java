package com.cloudapp.cloud_app.cloud_app.model.Users;

import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;

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

    public AvailibilityStatus status;

    // technician's rating

    private double rating;

    @Enumerated(EnumType.STRING)

    public Servicecategory work;

    public void setAvailibility(AvailibilityStatus newStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAvailibility'");
    }

}