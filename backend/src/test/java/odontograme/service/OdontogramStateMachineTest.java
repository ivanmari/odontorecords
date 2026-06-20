package odontograme.service;

import odontograme.patientrecords.Patient;
import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.repository.PatientRepository;
import odontograme.repository.PracticeRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import odontograme.patientrecords.odontogram.ToothStatusEvent;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class OdontogramStateMachineTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PracticeRepository practiceRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private String patientId;
    private Patient patient;

    @BeforeEach
    public void setup() {
        patientId = new ObjectId().toHexString();
        patient = new Patient();
        patient.setId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
    }

    @Test
    public void extractionOverridesPreviousFilling() throws Exception {
        Practice p1 = new Practice(Practice.Code.FillingBack, Instant.parse("2023-01-01T10:00:00Z"), 100);
        p1.setPieces("11 o");
        p1.setDone(true);

        Practice p2 = new Practice(Practice.Code.Extraction, Instant.parse("2023-02-01T10:00:00Z"), 200);
        p2.setPieces("11");
        p2.setDone(true);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(p1, p2)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Removed);
        assertThat(tooth.getFace(Tooth.ToothFaceName.Oclusal).isFilled()).isFalse();
    }

    @Test
    public void implantFollowsExtraction() throws Exception {
        Practice p1 = new Practice(Practice.Code.Extraction, Instant.parse("2023-01-01T10:00:00Z"), 200);
        p1.setPieces("11");
        p1.setDone(true);

        Practice p2 = new Practice(Practice.Code.Implant, Instant.parse("2023-02-01T10:00:00Z"), 1000);
        p2.setPieces("11");
        p2.setDone(true);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(p1, p2)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Implant);
    }

    @Test
    public void crownOverridesFilling() throws Exception {
        Practice p1 = new Practice(Practice.Code.FillingBack, Instant.parse("2023-01-01T10:00:00Z"), 100);
        p1.setPieces("11 o");
        p1.setDone(true);

        Practice p2 = new Practice(Practice.Code.Crown, Instant.parse("2023-02-01T10:00:00Z"), 500);
        p2.setPieces("11");
        p2.setDone(true);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(p1, p2)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Crown);
        assertThat(tooth.getFace(Tooth.ToothFaceName.Oclusal).isFilled()).isFalse();
    }

    @Test
    public void fillingsAddUp() throws Exception {
        Practice p1 = new Practice(Practice.Code.FillingBack, Instant.parse("2023-01-01T10:00:00Z"), 100);
        p1.setPieces("11 o");
        p1.setDone(true);

        Practice p2 = new Practice(Practice.Code.FillingBack, Instant.parse("2023-02-01T10:00:00Z"), 100);
        p2.setPieces("11 v");
        p2.setDone(true);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(p1, p2)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Filling);
        assertThat(tooth.getFace(Tooth.ToothFaceName.Oclusal).isFilled()).isTrue();
        assertThat(tooth.getFace(Tooth.ToothFaceName.Vestibular).isFilled()).isTrue();
    }

    @Test
    public void bridgeOverridesRemoved() throws Exception {
        Practice p1 = new Practice(Practice.Code.Extraction, Instant.parse("2023-01-01T10:00:00Z"), 200);
        p1.setPieces("11");
        p1.setDone(true);

        Practice p2 = new Practice(Practice.Code.Bridge, Instant.parse("2023-02-01T10:00:00Z"), 800);
        p2.setPieces("11BS");
        p2.setDone(true);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(p1, p2)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.BridgeStart);
    }

    @Test
    public void statusEventsAndPracticesAreAppliedChronologically() throws Exception {
        // 1. Status event: Filling in oclusal on 2023-01-01
        ToothStatusEvent s1 = new ToothStatusEvent(Instant.parse("2023-01-01T10:00:00Z"), 11, Tooth.ToothFaceName.Oclusal, true, false);

        // 2. Practice: Filling in vestibular on 2023-02-01
        Practice p1 = new Practice(Practice.Code.FillingBack, Instant.parse("2023-02-01T10:00:00Z"), 100);
        p1.setPieces("11 v");
        p1.setDone(true);

        // 3. Status event: Extraction on 2023-03-01
        ToothStatusEvent s2 = new ToothStatusEvent(Instant.parse("2023-03-01T10:00:00Z"), 11, Tooth.ToothStatus.Removed, false);

        // 4. Practice: Implant on 2023-04-01
        Practice p2 = new Practice(Practice.Code.Implant, Instant.parse("2023-04-01T10:00:00Z"), 1000);
        p2.setPieces("11");
        p2.setDone(true);

        patient.getStatusHistory().add(s1);
        patient.getStatusHistory().add(s2);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(p1, p2)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        // Final state should be Implant, and no faces should be filled (due to extraction in between)
        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Implant);
        assertThat(tooth.getFace(Tooth.ToothFaceName.Oclusal).isFilled()).isFalse();
        assertThat(tooth.getFace(Tooth.ToothFaceName.Vestibular).isFilled()).isFalse();
    }

    @Test
    public void fillingsAddUpBetweenPracticeAndStatusEvent() throws Exception {
        // 1. Practice: Filling in oclusal on 2023-01-01
        Practice p1 = new Practice(Practice.Code.FillingBack, Instant.parse("2023-01-01T10:00:00Z"), 100);
        p1.setPieces("11 o");
        p1.setDone(true);

        // 2. Status event: Filling in vestibular on 2023-02-01
        ToothStatusEvent s1 = new ToothStatusEvent(Instant.parse("2023-02-01T10:00:00Z"), 11, Tooth.ToothFaceName.Vestibular, true, false);

        patient.getStatusHistory().add(s1);

        when(practiceRepository.findByPatientId(eq(patientId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(p1)));

        Mouth mouth = patientService.getMouth(patientId);
        Tooth tooth = mouth.getToothByID(11);

        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Filling);
        assertThat(tooth.getFace(Tooth.ToothFaceName.Oclusal).isFilled()).isTrue();
        assertThat(tooth.getFace(Tooth.ToothFaceName.Vestibular).isFilled()).isTrue();
    }
}
