package odontograme.service;

import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
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
        return patientRepository.findById(id).map(patient -> {
            patient.setAccount(accountService);
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
            if (patientRepository.existsById(p.getId())) {
                patientRepository.save(p);
            } else {
                throw new PatientIdNotFoundException();
            }
        });
    }
}
