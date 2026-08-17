package com.cloudapp.cloud_app.cloud_app.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cloudapp.cloud_app.cloud_app.Repository.CustomerRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private TechnicianRequestRepository technicianRequestRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MatchingEngineService matchingEngineService;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    private Customer customer;
    private Technician technician;
    private ServiceRequest serviceRequest;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        technician = new Technician();
        technician.setStatus(AvailibilityStatus.Available);

        serviceRequest = new ServiceRequest();
        serviceRequest.setCustomer(customer);
        serviceRequest.setServicecategory(Servicecategory.AC_REPAIR);
        serviceRequest.setRequest(RequestStatus.CREATED);
    }

    @Test
    void createServiceRequest_savesARequestForAnExistingCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(serviceRequestRepository.save(any(ServiceRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ServiceRequest result = serviceRequestService.createServiceRequest(
                "Air conditioner is not cooling", 1L, Servicecategory.AC_REPAIR);

        assertSame(customer, result.getCustomer());
        assertEquals("Air conditioner is not cooling", result.getDescription());
        assertEquals(Servicecategory.AC_REPAIR, result.getServicecategory());
        assertEquals(RequestStatus.CREATED, result.getRequest());
        assertNotNull(result.getCreatedAt());
        verify(customerRepository).findById(1L);
        verify(serviceRequestRepository).save(result);
    }

    @Test
    void createServiceRequest_throwsWhenCustomerDoesNotExist() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> serviceRequestService.createServiceRequest(
                "Air conditioner is not cooling", 99L, Servicecategory.AC_REPAIR));

        verify(serviceRequestRepository, never()).save(any());
    }

    @Test
    void matchTechnician_assignsTheFirstEligibleTechnicianAndMarksThemBusy() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(technicianRequestRepository.findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, Servicecategory.AC_REPAIR))
                .thenReturn(List.of(technician));
        when(serviceRequestRepository.save(serviceRequest)).thenReturn(serviceRequest);

        ServiceRequest result = serviceRequestService.matchTechnician(1L);

        assertSame(technician, result.getTechnician());
        assertEquals(RequestStatus.ASSIGNED, result.getRequest());
        assertEquals(AvailibilityStatus.Busy, technician.getStatus());
        verify(serviceRequestRepository).save(serviceRequest);
    }

    @Test
    void matchTechnician_throwsWhenNoEligibleTechnicianExists() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(technicianRequestRepository.findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, Servicecategory.AC_REPAIR))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> serviceRequestService.matchTechnician(1L));

        verify(serviceRequestRepository, never()).save(any());
    }

    @Test
    void startJob_marksAnAssignedRequestAsInProgress() {
        serviceRequest.setTechnician(technician);
        serviceRequest.setRequest(RequestStatus.ASSIGNED);
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(serviceRequestRepository.save(serviceRequest)).thenReturn(serviceRequest);

        ServiceRequest result = serviceRequestService.startJob(1L);

        assertEquals(RequestStatus.IN_PROGRESS, result.getRequest());
        verify(serviceRequestRepository).save(serviceRequest);
    }

    @Test
    void startJob_throwsWhenNoTechnicianIsAssigned() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));

        assertThrows(IllegalArgumentException.class, () -> serviceRequestService.startJob(1L));

        verify(serviceRequestRepository, never()).save(any());
    }

    @Test
    void completeJob_marksTheRequestCompletedAndMakesTechnicianAvailable() {
        serviceRequest.setTechnician(technician);
        serviceRequest.setRequest(RequestStatus.IN_PROGRESS);
        technician.setStatus(AvailibilityStatus.Busy);
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(serviceRequestRepository.save(serviceRequest)).thenReturn(serviceRequest);

        ServiceRequest result = serviceRequestService.completeJob(1L);

        assertEquals(RequestStatus.COMPLETED, result.getRequest());
        assertEquals(AvailibilityStatus.Available, technician.getStatus());
        verify(serviceRequestRepository).save(serviceRequest);
    }
}
