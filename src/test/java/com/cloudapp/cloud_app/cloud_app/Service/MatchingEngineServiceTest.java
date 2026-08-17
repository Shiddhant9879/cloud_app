package com.cloudapp.cloud_app.cloud_app.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.request.RequestStatus;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

@ExtendWith(MockitoExtension.class)
class MatchingEngineServiceTest {

    @Mock
    private TechnicianRequestRepository technicianRequestRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private MatchingEngineService matchingEngineService;

    private ServiceRequest serviceRequest;
    private Technician technician;

    @BeforeEach
    void setUp() {
        serviceRequest = new ServiceRequest();
        serviceRequest.setId(1L);
        serviceRequest.setServicecategory(Servicecategory.AC_REPAIR);

        technician = new Technician();
        technician.setId(10L);
        technician.setStatus(AvailibilityStatus.Available);
    }

    @Test
    void getAvailableTechnicians_returnsEligibleTechniciansFromRepository() {
        when(technicianRequestRepository.findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, Servicecategory.AC_REPAIR))
                .thenReturn(List.of(technician));

        List<Technician> result = matchingEngineService.getAvailableTechnicians(Servicecategory.AC_REPAIR);

        assertEquals(List.of(technician), result);
        verify(technicianRequestRepository).findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, Servicecategory.AC_REPAIR);
    }

    @Test
    void assignTechnician_returnsTheFirstEligibleTechnician() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(technicianRequestRepository.findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, Servicecategory.AC_REPAIR))
                .thenReturn(List.of(technician));

        Technician result = matchingEngineService.AssignTechnician(1L);

        assertSame(technician, result);
    }

    @Test
    void assignTechnician_returnsNullWhenRequestDoesNotExist() {
        when(serviceRequestRepository.findById(99L)).thenReturn(Optional.empty());

        Technician result = matchingEngineService.AssignTechnician(99L);

        assertNull(result);
        verify(technicianRequestRepository, never())
                .findByIsVerifiedTrueAndStatusAndWork(any(), any());
    }

    @Test
    void assignTechnician_throwsWhenNoEligibleTechnicianExists() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(technicianRequestRepository.findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, Servicecategory.AC_REPAIR))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> matchingEngineService.AssignTechnician(1L));
    }

    @Test
    void updateRequestWithTechnician_assignsTechnicianAndSavesRequest() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(serviceRequestRepository.save(serviceRequest)).thenReturn(serviceRequest);

        ServiceRequest result = matchingEngineService.updateRequestWithTechnician(1L, technician);

        assertSame(technician, result.getTechnician());
        assertEquals(RequestStatus.ASSIGNED, result.getRequest());
        verify(serviceRequestRepository).save(serviceRequest);
    }

    @Test
    void updateRequestWithTechnician_returnsNullWhenRequestDoesNotExist() {
        when(serviceRequestRepository.findById(99L)).thenReturn(Optional.empty());

        ServiceRequest result = matchingEngineService.updateRequestWithTechnician(99L, technician);

        assertNull(result);
        verify(serviceRequestRepository, never()).save(any());
    }
}
