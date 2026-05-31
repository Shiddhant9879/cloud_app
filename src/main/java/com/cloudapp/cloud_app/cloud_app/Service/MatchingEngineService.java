package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;

import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import java.util.List;

@Service

public class MatchingEngineService {

    // repo object

    private final TechnicianRequestRepository technicianrequest;
    private final ServiceRequestRepository servicerequest;

    // constructor

    public MatchingEngineService(TechnicianRequestRepository technicianrequest,
            ServiceRequestRepository servicerequest) {

        this.technicianrequest = technicianrequest;
        this.servicerequest = servicerequest;
    }

    // method for eligible technician via the service request id

    public List<Technician> getEligibleTechniciansForRequest(long requestId) {

        Servicecategory category = servicerequest.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Service Request not found")).getServicecategory();

        return technicianrequest
                .findByIsVerifiedTrueAndAvailability(AvailibilityStatus.Available, category);
    }

    // method to assign the best technician to request

    public Technician assignTechnicianToRequest(long requestId) {

        // to check if the list is empty

        List<Technician> EligibleTechnicians = getEligibleTechniciansForRequest(requestId);

        if (EligibleTechnicians.isEmpty()) {

            throw new RuntimeException("No eligible technicians available for this request");
        }

        return EligibleTechnicians.get(0);
    }

}
