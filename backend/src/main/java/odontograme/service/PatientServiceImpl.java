package odontograme.service;

import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.patientrecords.odontogram.ToothFace;
import odontograme.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private PracticeCodesRepository practiceCodesRepository;

    private void injectServices(Patient patient) {
        patient.setAccount(accountService);
        patient.setPractices(practiceService);
    }

    public Page<Patient> findAll(Pageable p)
    {
        Page<Patient> patients = patientRepository.findAll(p);
        patients.forEach(this::injectServices);
        return patients;
    }

    @Override
    public Page<Patient> findByFirstName(String firstName, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByFirstName(firstName, pageable);
        patients.forEach(this::injectServices);
        return patients;
    }

    @Override
    public Page<Patient> findByLastNameLike(String lastName, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByLastNameLike(lastName, pageable);
        patients.forEach(this::injectServices);
        return patients;
    }

    @Override
    public Optional<Patient> findByPatientId(String id) {
        return patientRepository.findById(id).map(patient -> {
            injectServices(patient);
            return patient;
        });
    }

    @Override
    public void addPatient(Optional<Patient> patient) {
        patient.ifPresent(patientRepository::save);
    }

    @Override
    public void deletePatientById(String id) throws PatientIdNotFoundException {
        if(patientRepository.existsById(id))
        {
            patientRepository.deleteById(id);
        }
        else
        {
            throw new PatientIdNotFoundException();
        }
    }

    @Override
    public void updatePatient(Optional<Patient> patient) {
        patient.ifPresent(p -> {
            Patient existingPatient = patientRepository.findById(p.getIdStr()).orElseThrow(PatientIdNotFoundException::new);

            existingPatient.setDni(p.getDni());
            existingPatient.setFirstName(p.getFirstName());
            existingPatient.setLastName(p.getLastName());
            p.getAddress().ifPresent(existingPatient::setAddress);
            existingPatient.setSocialSecurityOrg(p.getSocialSecurityOrg());
            existingPatient.setSocialId(p.getSocialId());
            existingPatient.setBirthday(p.getBirthday());
            existingPatient.setGender(p.getGender());
            existingPatient.setPhone(p.getPhone());
            existingPatient.setComments(p.getComments());
            existingPatient.setFirstVisit(p.getFirstVisit());

            patientRepository.save(existingPatient);
        });
    }

    @Override
    public void updateToothStatus(String patientId, int toothId, Tooth.ToothStatus status, boolean planned) {
        patientRepository.findById(patientId).ifPresent(patient -> {
            try {
                Tooth tooth = patient.getMouth().getToothByID(toothId);
                tooth.setStatus(status);
                tooth.setPlanned(planned);
                patientRepository.save(patient);
            } catch (Exception e) {
                // Log error or handle appropriately
            }
        });
    }

    @Override
    public void updateToothFaceStatus(String patientId, int toothId, String faceName, boolean filled, boolean planned) {
        patientRepository.findById(patientId).ifPresent(patient -> {
            try {
                Tooth tooth = patient.getMouth().getToothByID(toothId);
                Tooth.ToothFaceName faceEnum = Tooth.ToothFaceName.valueOf(faceName);
                ToothFace face = tooth.getFace(faceEnum);
                face.setFilled(filled);
                face.setPlanned(planned);
                patientRepository.save(patient);
            } catch (Exception e) {
                // Log error or handle appropriately
            }
        });
    }
}
