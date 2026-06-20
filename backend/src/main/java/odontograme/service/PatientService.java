package odontograme.service;

import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.odontogram.Tooth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientService {
    Page<Patient> findAll(Pageable p);
    Page<Patient> findByFirstName(String firstName, Pageable pageable);
    Page<Patient> findByLastNameLike(String lastName, Pageable pageable);
    Optional<Patient> findByPatientId(String id);
    void addPatient(Optional<Patient> patient);
    void deletePatientById(String id) throws PatientIdNotFoundException;
    void updatePatient(Optional<Patient> patient);
    void updateToothStatus(String patientId, int toothId, Tooth.ToothStatus status, boolean planned);
    void updateToothFaceStatus(String patientId, int toothId, String faceName, boolean filled, boolean planned);
    odontograme.patientrecords.odontogram.Mouth getMouth(String patientId);
}
