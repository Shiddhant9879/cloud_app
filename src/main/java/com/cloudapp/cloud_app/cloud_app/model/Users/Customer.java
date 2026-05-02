package com.cloudapp.cloud_app.cloud_app.model.Users;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Customer {

    // user id

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    // linking it to the user table

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")

    private Users user;

    private String Address;

}