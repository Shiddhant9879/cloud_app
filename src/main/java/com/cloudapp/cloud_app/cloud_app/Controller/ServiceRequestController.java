package com.cloudapp.cloud_app.cloud_app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cloudapp.cloud_app.cloud_app.Service.TechnicianService;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.Service.ServiceRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/service-requests")

public class ServiceRequestController {

    private ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestservice) {

        this.serviceRequestService = serviceRequestservice;
    }

    // post mapping for the servicerequest

    @PostMapping("/create")

    public String createServiceRequest(@RequestBody ServiceRequest serviceRequest) {

        serviceRequestService.createServiceRequest(serviceRequest.getDescription(), serviceRequest.getCustomer());
        return "Service request created successfully";
    }

}
