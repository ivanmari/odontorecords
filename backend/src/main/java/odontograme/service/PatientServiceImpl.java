package odontograme.service;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.patientrecords.odontogram.ToothFace;
import odontograme.patientrecords.odontogram.ToothStatusEvent;
import odontograme.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    public void updateToothStatus(String patientId, int toothId, Tooth.ToothStatus status, boolean planned, Instant date) {
        patientRepository.findById(patientId).ifPresent(patient -> {
            if (date == null) {
                // Legacy behavior: update current mouth state directly
                try {
                    Tooth tooth = patient.getMouth().getToothByID(toothId);
                    tooth.setStatus(status);
                    tooth.setPlanned(planned);
                } catch (Exception e) {}
            } else {
                patient.getStatusHistory().add(new ToothStatusEvent(date, toothId, status, planned));
            }
            patientRepository.save(patient);
        });
    }

    @Override
    public void updateToothFaceStatus(String patientId, int toothId, String faceName, boolean filled, boolean planned, Instant date) {
        patientRepository.findById(patientId).ifPresent(patient -> {
            Tooth.ToothFaceName faceEnum = Tooth.ToothFaceName.valueOf(faceName);
            if (date == null) {
                // Legacy behavior
                try {
                    Tooth tooth = patient.getMouth().getToothByID(toothId);
                    ToothFace face = tooth.getFace(faceEnum);
                    face.setFilled(filled);
                    face.setPlanned(planned);
                } catch (Exception e) {}
            } else {
                patient.getStatusHistory().add(new ToothStatusEvent(date, toothId, faceEnum, filled, planned));
            }
            patientRepository.save(patient);
        });
    }

    @Override
    public Mouth getMouth(String patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(PatientIdNotFoundException::new);
        Mouth mouth = patient.getMouth(); // Maintain backward compatibility with legacy statuses

        List<Practice> practices = practiceRepository.findByPatientId(patientId, Pageable.unpaged()).getContent();
        List<ToothStatusEvent> statusEvents = patient.getStatusHistory();

        List<TimelineEventEntry> timeline = new ArrayList<>();
        for (Practice p : practices) {
            timeline.add(new TimelineEventEntry(p.getDeliveryDate(), p));
        }
        for (ToothStatusEvent s : statusEvents) {
            timeline.add(new TimelineEventEntry(s.getDate(), s));
        }

        timeline.sort(Comparator.comparing(e -> e.date != null ? e.date : Instant.EPOCH));

        for (TimelineEventEntry te : timeline) {
            if (te.event instanceof Practice) {
                applyPracticeToMouth(mouth, (Practice) te.event);
            } else if (te.event instanceof ToothStatusEvent) {
                applyStatusEventToMouth(mouth, (ToothStatusEvent) te.event);
            }
        }

        return mouth;
    }

    private static class TimelineEventEntry {
        Instant date;
        Object event;
        TimelineEventEntry(Instant date, Object event) {
            this.date = date;
            this.event = event;
        }
    }

    private void applyStatusEventToMouth(Mouth mouth, ToothStatusEvent event) {
        try {
            Tooth tooth = mouth.getToothByID(event.getToothId());
            if (event.getStatus() != null) {
                tooth.setStatus(event.getStatus());
                tooth.setPlanned(event.isPlanned());
                if (event.getStatus() == Tooth.ToothStatus.Removed ||
                    event.getStatus() == Tooth.ToothStatus.Implant ||
                    event.getStatus() == Tooth.ToothStatus.Crown) {
                    clearFaces(tooth);
                }
            } else if (event.getFace() != null) {
                if (tooth.getStatus() == Tooth.ToothStatus.Healthy ||
                    tooth.getStatus() == Tooth.ToothStatus.Caries ||
                    tooth.getStatus() == Tooth.ToothStatus.Filling) {
                    tooth.setStatus(Tooth.ToothStatus.Filling);
                    ToothFace face = tooth.getFace(event.getFace());
                    face.setFilled(event.getFilled());
                    face.setPlanned(event.isPlanned());
                }
            }
        } catch (Exception e) {
            // Ignore invalid tooth IDs
        }
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
