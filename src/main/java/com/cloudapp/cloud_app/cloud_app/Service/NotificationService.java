package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import java.util.List;

@Service

public class NotificationService {

    // workflow 1 technician request notification

    private final TechnicianRequestRepository TechnicianRequestRepository;
    private final ServiceRequestRepository ServiceRequestRepository;

    // constructor

    public NotificationService(TechnicianRequestRepository technicianRequestRepository,
            ServiceRequestRepository serviceRequestRepository) {

        this.TechnicianRequestRepository = technicianRequestRepository;
        this.ServiceRequestRepository = serviceRequestRepository;
    }

    // phase 1 request assigned to be notified to technician

    public void notifyTehnicianRequest(Technician technician, ServiceRequest serviceRequest) {

        ServiceRequest request = ServiceRequestRepository.findById(serviceRequest.getId()).orElse(null);

        if (request == null) {

            throw new RuntimeException("Service Request not found");
        } else {

            Technician technician1 = TechnicianRequestRepository.findById(technician.getId()).orElse(null);

            if (technician1 == null) {

                throw new RuntimeException("Technician not found");
            } else {

                System.out.println("Notification sent to technician:" + technician1.getName()
                        + "for the service request" + serviceRequest.getId());
            }

        }

    }

    // phase

}
