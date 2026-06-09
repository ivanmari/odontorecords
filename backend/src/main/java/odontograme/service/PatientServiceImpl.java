package odontograme.service;

import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PracticeCodesRepository practiceCodesRepository;

    public Page<Patient> findAll(Pageable p)
    {
        return patientRepository.findAll(p);
    }

    @Override
    public Page<Patient> findByFirstName(String firstName, Pageable pageable) {
        return patientRepository.findByFirstName(firstName, pageable);
    }

    @Override
    public Page<Patient> findByLastNameLike(String lastName, Pageable pageable) {
        return patientRepository.findByLastNameLike(lastName, pageable);
    }

    @Override
    public Optional<Patient> findByPatientId(String id) {
        Patient patient = patientRepository.findById(id);
        patient.setAccount(accountService);
        return Optional.of(patient);
    }

    @Override
    public void addPatient(Optional<Patient> patient) {
        patient.ifPresent(patientRepository::save);
    }

    @Override
    public void deletePatientById(String id) throws PatientIdNotFoundException {
        if(patientRepository.exists(id))
        {
            patientRepository.delete(id);
        }
        else
        {
            throw new PatientIdNotFoundException();
        }
    }

    @Override
    public void updatePatient(Optional<Patient> patient) {

        patientRepository.save(patient
                        .filter(p -> patientRepository.exists(p.getId()))
                        .orElseThrow(PatientIdNotFoundException::new));
    }
}
