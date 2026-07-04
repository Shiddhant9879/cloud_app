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
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceRequestService {

  // repositories

  private final TechnicianRequestRepository technicianRequestRepository;
  private final ServiceRequestRepository serviceRequestRepository;
  private final CustomerRepository customerRepository;
  private final MatchingEngineService matchingEngineService;

  // constructor injection

  public ServiceRequestService(
      ServiceRequestRepository serviceRequestRepository,
      TechnicianRequestRepository technicianRequestRepository,
      CustomerRepository customerRepository,
      MatchingEngineService matchingEngineService) {

    this.serviceRequestRepository = serviceRequestRepository;
    this.technicianRequestRepository = technicianRequestRepository;
    this.customerRepository = customerRepository;
    this.matchingEngineService = matchingEngineService;
  }

  // CREATE SERVICE REQUEST

  public ServiceRequest createServiceRequest(
      String description,
      Customer customer,
      Servicecategory serviceCategory) {

    ServiceRequest request = new ServiceRequest();

    request.setCustomer(customer);
    request.setDescription(description);
    request.setServicecategory(serviceCategory);
    request.setCreatedAt(LocalDateTime.now());
    request.setRequest(RequestStatus.CREATED);

    return serviceRequestRepository.save(request);
  }

  // Matching techncian synced via the matching engine service

  public ServiceRequest matchTechnician(Long requestId) {

    ServiceRequest request = serviceRequestRepository.findById(requestId)
        .orElseThrow(() -> new IllegalArgumentException("Request not found"));

    List<Technician> technicians = technicianRequestRepository
        .findByIsVerifiedTrueAndStatusAndWork(
            AvailibilityStatus.Available,
            request.getServicecategory());

    if (technicians.isEmpty()) {
      throw new RuntimeException(
          "No available technicians for this service category");
    }

    Technician technician = technicians.get(0);

    request.setTechnician(technician);
    request.setRequest(RequestStatus.ASSIGNED);

    technician.setStatus(AvailibilityStatus.Busy);

    return serviceRequestRepository.save(request);
  }
  // START JOB

  public ServiceRequest startJob(Long requestId) {

    ServiceRequest request = serviceRequestRepository.findById(requestId)
        .orElseThrow(() -> new IllegalArgumentException("Request not found"));

    if (request.getTechnician() == null) {

      throw new IllegalArgumentException(
          "No technician assigned");
    }

    request.setRequest(RequestStatus.IN_PROGRESS);

    return serviceRequestRepository.save(request);
  }

  // COMPLETE JOB

  public ServiceRequest completeJob(Long requestId) {

    ServiceRequest request = serviceRequestRepository.findById(requestId)
        .orElseThrow(() -> new IllegalArgumentException("Request not found"));

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
