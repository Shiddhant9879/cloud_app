package com.cloudapp.cloud_app.cloud_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import java.util.List;

public interface TechnicianRequestRepository extends JpaRepository<Technician, Long> {

    List<Technician> findByIsVerifiedTrueAndAvailability(AvailibilityStatus Status, Servicecategory work);

}
