package com.cloudapp.cloud_app.cloud_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}