package com.cloudapp.cloud_app.cloud_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
