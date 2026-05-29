package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

@Service

public class TechnicianService {

    private TechnicianRequestRepository technicianrequest;

    // construcutor

    public TechnicianService(TechnicianRequestRepository technicianrequest) {

        this.technicianrequest = technicianrequest;
    }

    // method for all the available technicians

    public List<Technician> getAvailableTechicians() {

        return technicianrequest.findByIsVerifiedTrueAndAvailability(AvailibilityStatus.Available);
    }

    // update technician availability

    public Technician updateTechnicianAvailibility(long technicianId, AvailibilityStatus newStatus) {

        Technician technician = technicianrequest.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        technician.setAvailibility(newStatus);
        return technicianrequest.save(technician);
    }

    // get technician by id

    public Technician getTechnicianById(long technicianId) {

        return technicianrequest.findById(technicianId).orElseThrow(() -> new RuntimeException("Technician not found"));
    }

}