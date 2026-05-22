package com.cloudapp.cloud_app.cloud_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByStatus(RequestStatus Status);

}