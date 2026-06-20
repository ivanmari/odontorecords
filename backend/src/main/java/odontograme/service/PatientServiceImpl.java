package odontograme.service;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.patientrecords.odontogram.ToothFace;
import odontograme.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private PracticeRepository practiceRepository;

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
                logger.error("Error updating tooth status for patient " + patientId + ", tooth " + toothId, e);
            }
        });
    }

    @Override
    public void updateToothFacesStatus(String patientId, int toothId, java.util.List<ToothFace> faces) {
        patientRepository.findById(patientId).ifPresent(patient -> {
            try {
                Tooth tooth = patient.getMouth().getToothByID(toothId);
                for (ToothFace faceUpdate : faces) {
                    ToothFace face = tooth.getFace(faceUpdate.getFaceName());
                    face.setFilled(faceUpdate.isFilled());
                    face.setPlanned(faceUpdate.isPlanned());
                }
                patientRepository.save(patient);
            } catch (Exception e) {
                logger.error("Error updating tooth faces status for patient " + patientId + ", tooth " + toothId, e);
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
                logger.error("Error updating tooth face status for patient " + patientId + ", tooth " + toothId + ", face " + faceName, e);
            }
        });
    }

    @Override
    public Mouth getMouth(String patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(PatientIdNotFoundException::new);
        Mouth mouth = patient.getMouth();

        java.util.List<Practice> practices = practiceRepository.findByPatientId(patientId, org.springframework.data.domain.Pageable.unpaged()).getContent();
        java.util.List<Practice> sortedPractices = new java.util.ArrayList<>(practices);
        sortedPractices.sort(java.util.Comparator.comparing(Practice::getDeliveryDate));

        for (Practice p : sortedPractices) {
            applyPracticeToMouth(mouth, p);
        }

        return mouth;
    }

    private void applyPracticeToMouth(Mouth mouth, Practice practice) {
        java.util.List<Practice.AffectedPiece> affectedPieces = practice.getAffectedPiecesList();
        for (Practice.AffectedPiece ap : affectedPieces) {
            try {
                Tooth tooth = mouth.getToothByID(ap.toothNumber);
                boolean isPlanned = practice.isPlanned();

                switch (practice.getCode()) {
                    case Extraction:
                        tooth.setStatus(Tooth.ToothStatus.Removed);
                        tooth.setPlanned(isPlanned);
                        clearFaces(tooth);
                        break;
                    case Implant:
                        tooth.setStatus(Tooth.ToothStatus.Implant);
                        tooth.setPlanned(isPlanned);
                        clearFaces(tooth);
                        break;
                    case Crown:
                        tooth.setStatus(Tooth.ToothStatus.Crown);
                        tooth.setPlanned(isPlanned);
                        clearFaces(tooth);
                        break;
                    case FillingFront:
                    case FillingBack:
                        if (tooth.getStatus() == Tooth.ToothStatus.Healthy ||
                            tooth.getStatus() == Tooth.ToothStatus.Caries ||
                            tooth.getStatus() == Tooth.ToothStatus.Filling) {
                            tooth.setStatus(Tooth.ToothStatus.Filling);
                            for (Tooth.ToothFaceName faceName : ap.faces) {
                                ToothFace face = tooth.getFace(faceName);
                                face.setFilled(true);
                                face.setPlanned(isPlanned);
                            }
                        }
                        break;
                    case Bridge:
                        if (ap.bridgeStatus != null) {
                            tooth.setStatus(ap.bridgeStatus);
                            tooth.setPlanned(isPlanned);
                        }
                        break;
                    default:
                        // Other practices like Cleaning or RootCanal don't necessarily change the tooth status in the odontogram for now
                        break;
                }
            } catch (Exception e) {
                // Ignore invalid tooth IDs
            }
        }
    }

    private void clearFaces(Tooth tooth) {
        for (ToothFace face : tooth.getFaces()) {
            face.setFilled(false);
            face.setPlanned(false);
        }
    }
}
