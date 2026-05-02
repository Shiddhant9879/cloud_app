package com.cloudapp.cloud_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

}