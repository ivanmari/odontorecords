package odontograme.rest;

import odontograme.service.PatientService;
import odontograme.service.AccountService;
import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.patientrecords.exceptions.PatientIdNotFoundException;
import odontograme.patientrecords.Patient;
import odontograme.viewmodel.bookkeepingview.AccountStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
public class BillingController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/patient/{patientId}/account/charges", method = RequestMethod.POST)
    ResponseEntity<?> addCharge(@PathVariable String patientId, @RequestBody Charge chargeFromRest) {
        ResponseEntity responseEntity = ResponseEntity.noContent().build();

        Patient patient = patientService.findByPatientId(patientId).orElseThrow(PatientIdNotFoundException::new);
        chargeFromRest.setPatientId(patientId);
        accountService.addCharge(chargeFromRest);
        responseEntity = ResponseEntity.status(HttpStatus.CREATED).build();

        return responseEntity;
    }

    @RequestMapping(value = "/patient/{patientId}/account/charges", method = RequestMethod.GET)
    public Page<Charge> getCharges(@PathVariable String patientId, Pageable pageable) {
        return accountService.getCharges(patientId, pageable);
    }

    @RequestMapping(value = "/patient/{patientId}/account/charges/{chargeId}", method = RequestMethod.DELETE)
    ResponseEntity<?> delCharge(@PathVariable String patientId, @PathVariable String chargeId) {
        ResponseEntity responseEntity = ResponseEntity.badRequest().build();

        try {
            Patient patient = patientService.findByPatientId(patientId).orElseThrow(PatientIdNotFoundException::new);
            patient.removeCharge(chargeId);
            responseEntity = ResponseEntity.noContent().build();
        } catch (InstallmentPresentException e) {
            responseEntity = ResponseEntity.unprocessableEntity().build();
        }

        return responseEntity;
    }

    @RequestMapping(value = "/patient/{patientId}/account/installments", method = RequestMethod.POST)
    ResponseEntity<?> addInstallment(@PathVariable String patientId, @RequestBody Installment installmentFromRest) {
        ResponseEntity responseEntity = ResponseEntity.noContent().build();

        Patient patient = patientService.findByPatientId(patientId).orElseThrow(PatientIdNotFoundException::new);
        installmentFromRest.setPatientId(patientId);
        accountService.addInstallment(installmentFromRest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(patient.getDni()).toUri();

        responseEntity = ResponseEntity.created(location).build();

        return responseEntity;
    }

    @RequestMapping(value = "/patient/{patientId}/account/installments", method = RequestMethod.GET)
    public Page<Installment> getInstallments(@PathVariable String patientId, Pageable pageable) {
        return accountService.getInstallments(patientId, pageable);
    }

    @RequestMapping(value = "/patient/{patientId}/account/installments/{installmentId}", method = RequestMethod.DELETE)
    ResponseEntity<?> delInstallment(@PathVariable String patientId, @PathVariable String installmentId) {
        ResponseEntity responseEntity = ResponseEntity.noContent().build();

        Patient patient = patientService.findByPatientId(patientId).orElseThrow(PatientIdNotFoundException::new);
        try {
            patient.removeInstallment(installmentId);
            responseEntity = ResponseEntity.noContent().build();
        } catch (InstallmentIdNotFoundException e) {
            responseEntity = ResponseEntity.unprocessableEntity().build();
        }

        return responseEntity;
    }

    @RequestMapping(value = "/patient/{patientId}/balance", method = RequestMethod.GET)
    public AccountStat getPatientBalance(@PathVariable String patientId) {

        int balance = accountService.getBalance(patientId);

        return new AccountStat(balance);
    }

}
