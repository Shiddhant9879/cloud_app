package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;

import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import java.util.List;

@Service

public class MatchingEngineService {

    // contrucor

    private final TechnicianRequestRepository technicianRequestRepository;
    private final ServiceRequestRepository servicerequestRepository;

    public MatchingEngineService(TechnicianRequestRepository technicianRequestRepository,
            ServiceRequestRepository servicerequestRepository) {

        this.technicianRequestRepository = technicianRequestRepository;
        this.servicerequestRepository = servicerequestRepository;

    }

    // phase 1 : get all available technicians and service requests plus this should
    // add category filter also

    public List<Technician> getAvailableTechnicians(Servicecategory servicecategory) {

        return technicianRequestRepository.findByIsVerifiedTrueAndAvailability(AvailibilityStatus.Available,
                servicecategory);
    }

    // phase 2 : now out of the available technician who would get the job

    public Technician AssignTechnician(Long requestId) {

        ServiceRequest request = servicerequestRepository.findById(requestId).orElse(null);

        if (request == null) {

            return null;
        }

        List<Technician> availableTechnicians = getAvailableTechnicians(request.getServicecategory());

        if (availableTechnicians.isEmpty()) {

            throw new RuntimeException("No available technician is found for the request");
        } else {

            Technician assignedTechnician = availableTechnicians.get(0);
            return assignedTechnician;
        }

    }

    // phase 3 : now we have the assigned technician and the request now we can
    // update the request with the assigned technician

    public ServiceRequest updateRequestWithTechnician(Long requestId, Technician assignedTechnician) {

        ServiceRequest request = servicerequestRepository.findById(requestId).orElse(null);

        if (request == null) {

            return null;
        } else {

            request.setTechnician(assignedTechnician);
            request.setRequest(RequestStatus.ASSIGNED);
            return servicerequestRepository.save(request);
        }
    }

}
