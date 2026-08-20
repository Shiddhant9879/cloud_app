package com.cloudapp.cloud_app.cloud_app.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cloudapp.cloud_app.cloud_app.Repository.ServiceRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.request.ServiceRequest;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private TechnicianRequestRepository technicianRequestRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Technician technician;
    private ServiceRequest serviceRequest;

    @BeforeEach
    void setUp() {
        technician = new Technician();
        technician.setId(10L);
        technician.setName("Alex");

        serviceRequest = new ServiceRequest();
        serviceRequest.setId(1L);
    }

    @Test
    void notifyTehnicianRequest_checksRequestAndTechnicianBeforeSendingNotification() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(technicianRequestRepository.findById(10L)).thenReturn(Optional.of(technician));

        assertDoesNotThrow(() -> notificationService.notifyTehnicianRequest(technician, serviceRequest));

        verify(serviceRequestRepository).findById(1L);
        verify(technicianRequestRepository).findById(10L);
    }

    @Test
    void notifyTehnicianRequest_throwsWhenServiceRequestDoesNotExist() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> notificationService.notifyTehnicianRequest(technician, serviceRequest));
    }

    @Test
    void notifyTehnicianRequest_throwsWhenTechnicianDoesNotExist() {
        when(serviceRequestRepository.findById(1L)).thenReturn(Optional.of(serviceRequest));
        when(technicianRequestRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> notificationService.notifyTehnicianRequest(technician, serviceRequest));
    }
}
