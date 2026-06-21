package com.cloudapp.cloud_app.cloud_app.Service;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.springframework.stereotype.Service;
import java.util.List;

import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

@Service

public class TechnicianService {

    private TechnicianRequestRepository technicianrequest;
    private Servicecategory servicecategory;

    // construcutor

    public TechnicianService(TechnicianRequestRepository technicianrequest) {

        this.technicianrequest = technicianrequest;
    }

    // method for all the available technicians

    public List<Technician> getAvailableTechicians() {

        return technicianrequest.findByIsVerifiedTrueAndAvailability(AvailibilityStatus.Available, null);
    }

    // update technician availability

    public Technician updateTechnicianAvailibility(long technicianId, AvailibilityStatus newStatus) {

        Technician technician = technicianrequest.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        technician.setStatus(newStatus);
        return technicianrequest.save(technician);
    }

    // get technician by id

    public Technician getTechnicianById(long technicianId) {

        return technicianrequest.findById(technicianId).orElseThrow(() -> new RuntimeException("Technician not found"));
    }

}