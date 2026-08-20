package com.cloudapp.cloud_app.cloud_app.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceTest {

    @Mock
    private TechnicianRequestRepository technicianRequestRepository;

    @InjectMocks
    private TechnicianService technicianService;

    @Test
    void getAvailableTechicians_returnsAvailableVerifiedTechnicians() {
        Technician technician = technician(1L, AvailibilityStatus.Available);
        when(technicianRequestRepository.findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, null))
                .thenReturn(List.of(technician));

        List<Technician> result = technicianService.getAvailableTechicians();

        assertEquals(List.of(technician), result);
        verify(technicianRequestRepository).findByIsVerifiedTrueAndStatusAndWork(
                AvailibilityStatus.Available, null);
    }

    @Test
    void updateTechnicianAvailibility_updatesAndSavesExistingTechnician() {
        Technician technician = technician(1L, AvailibilityStatus.NotAvailable);
        when(technicianRequestRepository.findById(1L)).thenReturn(Optional.of(technician));
        when(technicianRequestRepository.save(technician)).thenReturn(technician);

        Technician result = technicianService.updateTechnicianAvailibility(1L, AvailibilityStatus.Busy);

        assertSame(technician, result);
        assertEquals(AvailibilityStatus.Busy, result.getStatus());
        verify(technicianRequestRepository).save(technician);
    }

    @Test
    void updateTechnicianAvailibility_throwsWhenTechnicianDoesNotExist() {
        when(technicianRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> technicianService.updateTechnicianAvailibility(99L, AvailibilityStatus.Busy));
    }

    @Test
    void getTechnicianById_returnsExistingTechnician() {
        Technician technician = technician(1L, AvailibilityStatus.Available);
        when(technicianRequestRepository.findById(1L)).thenReturn(Optional.of(technician));

        Technician result = technicianService.getTechnicianById(1L);

        assertSame(technician, result);
    }

    @Test
    void getTechnicianById_throwsWhenTechnicianDoesNotExist() {
        when(technicianRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> technicianService.getTechnicianById(99L));
    }

    private Technician technician(Long id, AvailibilityStatus status) {
        Technician technician = new Technician();
        technician.setId(id);
        technician.setStatus(status);
        return technician;
    }
}
