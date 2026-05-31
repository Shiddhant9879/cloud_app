package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;

import com.cloudapp.cloud_app.cloud_app.Repository.CustomerRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;

import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceRequestService {

  // repositories

  private final TechnicianRequestRepository technicianRequestRepository;
  private final ServiceRequestRepository serviceRequestRepository;
  private final CustomerRepository customerRepository;

  // constructor injection

  public ServiceRequestService(
      ServiceRequestRepository serviceRequestRepository,
      TechnicianRequestRepository technicianRequestRepository,
      CustomerRepository customerRepository) {

    this.serviceRequestRepository = serviceRequestRepository;
    this.technicianRequestRepository = technicianRequestRepository;
    this.customerRepository = customerRepository;
  }

  // CREATE SERVICE REQUEST

  public ServiceRequest createServiceRequest(
      String description,
      Customer customer) {

    ServiceRequest request = new ServiceRequest();

    request.setCustomer(customer);
    request.setDescription(description);
    request.setCreatedAt(LocalDateTime.now());
    request.setRequest(RequestStatus.COMPLETED);

    return serviceRequestRepository.save(request);
  }

  // MATCH TECHNICIAN

  public ServiceRequest matchTechnician(ServiceRequest request) {

    List<Technician> technicians = technicianRequestRepository
        .findByIsVerifiedTrueAndAvailability(
            AvailibilityStatus.Available, null);

    if (technicians.isEmpty()) {

      throw new RuntimeException(
          "No available technicians at the moment");
    }

    Technician technician = technicians.get(0);

    request.setTechnician(technician);
    request.setRequest(RequestStatus.ASSIGNED);

    technician.setStatus(AvailibilityStatus.Busy);

    return serviceRequestRepository.save(request);
  }

  // START JOB

  public ServiceRequest startJob(ServiceRequest request) {

    if (request.getTechnician() == null) {

      throw new IllegalArgumentException(
          "No technician assigned");
    }

    request.setRequest(RequestStatus.IN_PROGRESS);

    return serviceRequestRepository.save(request);
  }

  // COMPLETE JOB

  public ServiceRequest completeJob(ServiceRequest request) {

    if (request.getTechnician() == null) {

      throw new IllegalArgumentException(
          "No technician assigned");
    }

    request.setRequest(RequestStatus.COMPLETED);

    Technician technician = request.getTechnician();

    technician.setStatus(AvailibilityStatus.Available);

    return serviceRequestRepository.save(request);
  }
}