package com.cloudapp.cloud_app.cloud_app.Service;

import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cloudapp.cloud_app.cloud_app.model.Users.AvailabilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository servicerequestRepository;
    private final TechnicianRequestRepository technicianRequestRepository;

    public ServiceRequestService(ServiceRequestRepository servicerequestRepository,
            TechnicianRequestRepository technicianrequestRepository) {

        this.servicerequestRepository = servicerequestRepository;
        this.technicianRequestRepository = technicianrequestRepository;
    }

    // make the request body here and then validate that here

    public ServiceRequest createServiceRequest(String description, Customer customer) {

        ServiceRequest request = new ServiceRequest();

        request.setCustomer(customer);
        request.setDescription(description);
        request.setCreatedAt(LocalDateTime.now());
        request.setRequest(RequestStatus.CREATED);

        return servicerequestRepository.save(request);
    }

    // method for matching all the technicians

    List<Technician>technicians = technicianRequestRepository.findByIsVerifiedTrueAndAvailability(AvailabilityStatus.AVAILABLE)
    {

        // if none of the condition matches

        if (!technicians.isEmpty()) {

            throw new IllegalArgumentException("technician not found");
        }

        Technician selected = technicians.get(0);

        return assignTechnician(request, selected);
    }

    // once the technician is selected

    public ServiceRequest assignTechnician(ServiceRequest request, Technician technician) {

        if (!technician.isVerified()) {
            throw new RuntimeException("Technician not verified");
        }

        if (technician.getAvailability() != AvailabilityStatus.AVAILABLE) {
            throw new RuntimeException("Technician not available");
        }

        request.setTechnician(technician);
        request.setStatus(RequestStatus.ASSIGNED);

        technician.setAvailability(AvailabilityStatus.BUSY);

        return servicerequestRepository.save(request);
    }

}
