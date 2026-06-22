package com.cloudapp.cloud_app.cloud_app.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.Service.ServiceRequestService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/service-requests")

public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestservice) {

        this.serviceRequestService = serviceRequestservice;
    }

    // post mapping for the servicerequest

    @PostMapping("/create")

    public String createServiceRequest(@RequestBody ServiceRequest serviceRequest) {

        serviceRequestService.createServiceRequest(serviceRequest.getDescription(), serviceRequest.getCustomer());
        return "Service request created successfully";
    }

    // post mapping for matching technician using id

    @PostMapping("/match-technician/{requestId}")

    public String matchTechnician(@PathVariable Long requestId) {

        serviceRequestService.matchTechnician(requestId);
        return "Technician matched successfully";
    }

    // startjob endpoint

    @PostMapping("/start-job/{requestId}")

    public String startJob(@PathVariable Long requestId) {

        serviceRequestService.startJob(requestId);
        return "Job started successfully";
    }

    // completejob endpoint

    @PostMapping("/complete-job/{requestId}")

    public String completeJob(@PathVariable Long requestId) {

        serviceRequestService.completeJob(requestId);
        return "Job completed successfully";
    }

}
