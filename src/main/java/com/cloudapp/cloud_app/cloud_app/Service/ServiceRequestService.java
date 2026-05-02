package com.cloudapp.cloud_app.cloud_app.Service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import java.time.LocalDateTime;

@Service

public class ServiceRequestService {

    // creation of service request

    public ServiceRequest createServiceRequest(Customer customer, String description) {

        ServiceRequest request = new ServiceRequest();

        request.setCustomer(customer);
        request.setDescription(description);
        request.setCreatedAt(LocalDateTime.now());

        return request;

    }

    // technician matching

    public void assignTechnician(ServiceRequest request, Technician technician) {

        request.setTechnician(technician);
        request.verifyisVerified(technician);
    }

}
