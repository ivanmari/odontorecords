package odontograme.rest;

import odontograme.patientrecords.Patient;
import odontograme.patientrecords.Practice;
import odontograme.repository.PracticeRepository;
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
    private PracticeRepository practiceRepository;

    @RequestMapping(value = "/patient/{patientId}/practices", method = RequestMethod.GET)
    Page<Practice> getPractices(Pageable pageable, @PathVariable String patientId) {

        Page<Practice> practices = practiceRepository.findByPatientId(patientId, pageable);

        return practices;
    }

    @RequestMapping(value = "/patient/{patientId}/practice", method = RequestMethod.POST)
    ResponseEntity<?> addPractice(@PathVariable String patientId, @RequestBody PracticeRest input) {

        Practice practice = input.getPractice();

        ResponseEntity responseEntity = ResponseEntity.noContent().build();

        Practice saved_practice = practiceRepository.save(practice);

        if (saved_practice != null) {

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(saved_practice.getId()).toUri();
            responseEntity = ResponseEntity.created(location).build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;

    }

    public void setPracticeRepository(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

}
