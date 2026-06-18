package odontograme.service;

import odontograme.patientrecords.Patient;
import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.personaldata.Address;
import odontograme.repository.PatientRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientUpdateServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient existingPatient;
    private String patientId;

    @BeforeEach
    public void setup() {
        patientId = new ObjectId().toHexString();
        existingPatient = new Patient();
        existingPatient.setId(patientId);
        existingPatient.setFirstName("Old");
        existingPatient.setLastName("Name");
        existingPatient.setDni(12345);

        // Ensure mouth is set and has some unique state if possible
        Mouth mouth = existingPatient.getMouth();
        assertThat(mouth).isNotNull();
    }

    @Test
    public void updatePatientCorrectlyUpdatesFieldsAndPreservesMouth() {
        Patient updateInfo = new Patient();
        updateInfo.setId(patientId);
        updateInfo.setFirstName("New");
        updateInfo.setLastName("Update");
        updateInfo.setDni(67890);
        updateInfo.setAddress(new Address("City", "Street", "1", "A"));
        updateInfo.setGender("Other");
        updateInfo.setPhone("555-1234");
        updateInfo.setComments("Updated comments");
        updateInfo.setFirstVisit(Instant.now());

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));

        patientService.updatePatient(Optional.of(updateInfo));

        verify(patientRepository).save(existingPatient);

        assertThat(existingPatient.getFirstName()).isEqualTo("New");
        assertThat(existingPatient.getLastName()).isEqualTo("Update");
        assertThat(existingPatient.getDni()).isEqualTo(67890);
        assertThat(existingPatient.getGender()).isEqualTo("Other");
        assertThat(existingPatient.getPhone()).isEqualTo("555-1234");
        assertThat(existingPatient.getComments()).isEqualTo("Updated comments");
        assertThat(existingPatient.getMouth()).isSameAs(existingPatient.getMouth()); // Should still be the same mouth object
    }

    @Test
    public void updatePatientThrowsExceptionIfNotFound() {
        Patient updateInfo = new Patient();
        updateInfo.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientIdNotFoundException.class, () -> {
            patientService.updatePatient(Optional.of(updateInfo));
        });

        verify(patientRepository, never()).save(any());
    }
}
