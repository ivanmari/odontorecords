package odontograme.rest;

import odontograme.patientrecords.Practice;
import odontograme.service.PracticeService;
import odontograme.viewmodel.patientrecordview.PracticeRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
public class PracticeController {
    @Autowired
    private PracticeService practiceService;

    @RequestMapping(value = "/patient/{patientId}/practices", method = RequestMethod.GET)
    Page<Practice> getPractices(Pageable pageable, @PathVariable String patientId) {
        return practiceService.findByPatientId(patientId, pageable);
    }

    @RequestMapping(value = "/patient/{patientId}/practice", method = RequestMethod.POST)
    ResponseEntity<?> addPractice(@PathVariable String patientId, @RequestBody PracticeRest input) {

        input.setPatientId(patientId);
        Practice practice = input.getPractice();
        practiceService.addPractice(practice);

        if (practice.getId() != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(practice.getId()).toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
