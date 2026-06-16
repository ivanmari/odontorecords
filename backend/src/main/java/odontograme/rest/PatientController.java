package odontograme.rest;

import odontograme.service.AccountService;
import odontograme.service.PatientService;
import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.repository.PracticeCodesRepository;
import odontograme.service.PracticeService;
import odontograme.viewmodel.patientrecordview.PatientBasicInfo;
import odontograme.viewmodel.patientrecordview.PatientFullInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by immari on 9/15/2016.
 */

@CrossOrigin
@RestController
class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private PracticeCodesRepository practiceCodesRepository;

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    public Page<PatientBasicInfo> getAllPatients(Pageable pageable) {
        Page<Patient> patients_page = patientService.findAll(pageable);
        List<PatientBasicInfo> patients_basic = patients_page.getContent().stream().map(PatientBasicInfo::new).collect(Collectors.toList());
        return new PageImpl<PatientBasicInfo>(patients_basic, pageable, patients_page.getTotalElements() );
    }

    @RequestMapping(value = "/patient", method = RequestMethod.GET)
    public Page<PatientBasicInfo> getPatientsByLastName(@RequestParam(value="name", defaultValue="") String name, Pageable pageable) {
        Page<Patient> patient_info_page = patientService.findByLastNameLike(name, pageable);
        List<PatientBasicInfo> patients_info = patient_info_page.getContent().stream().map(PatientBasicInfo::new).collect(Collectors.toList());
        return new PageImpl<PatientBasicInfo>(patients_info, pageable, patient_info_page.getTotalElements() );
    }

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.GET)
    public PatientFullInfo getPatientById(@PathVariable String id) {
        return patientService.findByPatientId(id).map(PatientFullInfo::new).orElseThrow(PatientIdNotFoundException::new);
    }


    @RequestMapping(value = "/patient/{id}/graphmouth", method = RequestMethod.GET)
    public Mouth getPatientGraphicMouth(@PathVariable String id, @RequestParam(value="closingDate", defaultValue="") String closingDateStr, @RequestParam(value="healthProvider", defaultValue="Generic") String healthProvider){
        Patient patient = patientService.findByPatientId(id).orElseThrow(PatientIdNotFoundException::new);
        return patient.getMouth();
    }


    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.DELETE)
    ResponseEntity<?> delPatient(@PathVariable String patientId) {

        patientService.deletePatientById(patientId);

        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/patient", method = RequestMethod.POST)
    ResponseEntity<?> createPatientInfo(@RequestBody PatientFullInfo patientFullInfo) {
        ResponseEntity responseEntity = ResponseEntity.noContent().build();

        if (patientFullInfo != null) {
            patientService.addPatient(patientFullInfo.getPatient(accountService,practiceService));

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(patientFullInfo.getId()).toUri();

            responseEntity = ResponseEntity.created(location).build();
        }
        return responseEntity;
    }

    @RequestMapping(value = "/patient", method = RequestMethod.PUT)
    ResponseEntity<?> updateContactPatientInfo(@RequestBody PatientFullInfo patientFullInfo) {
        ResponseEntity responseEntity = ResponseEntity.noContent().build();

        if (patientFullInfo != null) {
            patientService.updatePatient(patientFullInfo.getPatient(accountService, practiceService));

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(patientFullInfo.getId()).toUri();

            responseEntity = ResponseEntity.created(location).build();
        }
        return responseEntity;
    }

    @RequestMapping(value = "/patient/{patientId}/tooth/{toothId}/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateToothStatus(@PathVariable String patientId, @PathVariable int toothId, @RequestParam Tooth.ToothStatus status, @RequestParam boolean planned) {
        patientService.updateToothStatus(patientId, toothId, status, planned);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/patient/{patientId}/tooth/{toothId}/face/{faceName}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateToothFaceStatus(@PathVariable String patientId, @PathVariable int toothId, @PathVariable String faceName, @RequestParam boolean filled, @RequestParam boolean planned) {
        patientService.updateToothFaceStatus(patientId, toothId, faceName, filled, planned);
        return ResponseEntity.ok().build();
    }

    public void setPracticeCodesRepository(PracticeCodesRepository practiceCodesRepository) {
        this.practiceCodesRepository = practiceCodesRepository;
    }
}
