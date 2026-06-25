package com.cloudapp.cloud_app.cloud_app.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

public interface TechnicianRequestRepository
        extends JpaRepository<Technician, Long> {

    List<Technician> findByIsVerifiedTrueAndStatusAndWork(
            AvailibilityStatus status,
            Servicecategory work);
}