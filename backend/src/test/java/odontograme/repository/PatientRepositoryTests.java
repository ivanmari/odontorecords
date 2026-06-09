package odontograme.repository;
/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import odontograme.bookkeeping.AccountServiceMock;
import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.patientrecords.PatientBuilder;
import odontograme.patientrecords.odontogram.PracticeServicesMock;
import odontograme.service.AccountService;
import odontograme.service.AccountServiceImpl;
import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.patientrecords.personaldata.Address;
import odontograme.service.PracticeService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.InvalidKeyException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientRepositoryTests {

    @Autowired
    private
    PatientRepository repository;

    private AccountService accountService = new AccountServiceMock();
    private PracticeService practiceService = new PracticeServicesMock();

    private Patient dave, oliver, carter;

    @Before
    public void setUp() {

        repository.deleteAll();

        dave = new PatientBuilder(accountService, practiceService).withName("Dave").withLastName("Mathews").build();;

        dave.setDni(1000);
        dave.setSocialId("123aa");

        repository.save(dave);

        oliver = new PatientBuilder(accountService, practiceService).withName("Oliver August").withLastName("Mathews").build();
        oliver.setDni(1001);
        oliver.setSocialId("123ab");
        dave.setBirthday(Instant.parse( "1990-12-03T10:15:30.00Z"));

        repository.save(oliver);

        carter = new PatientBuilder(accountService, practiceService).withName("Carter").withLastName("Beauford").build();
        carter.setDni(1002);
        carter.setSocialId("123ac");

        repository.save(carter);
    }

    @Test
    public void setsIdOnSave() {

        dave = new PatientBuilder(accountService, practiceService).build();

        dave.setDni(2000);
        dave.setFirstName("Dave");
        dave.setLastName("Matthews");
        dave.setSocialId("123aa");
        dave.setSocialSecurityOrg("WHO");
        dave.setBirthday(Instant.parse( "1990-12-03T10:15:30.00Z"));
        dave.setGender("masc");
        dave.setPhone("3513221908");
        dave.setComments("Sordid");

        Address address = new Address("Cordoba", "Irigoyen", "150", "5D");

        dave.setAddress(address);

        Charge charge = new Charge(new ObjectId().toString(), new Date(), 90, "filling");

        dave.addCharge(charge);

        repository.save(dave);

        Patient patient = repository.findById(dave.getIdStr());

        assertThat(patient.getDni()).isEqualTo(dave.getDni());
        assertThat(patient.getFirstName()).isEqualTo("Dave");
        assertThat(patient.getLastName()).isEqualTo("Matthews");
        assertThat(patient.getSocialId()).isEqualTo("123aa");
        assertThat(patient.getSocialSecurityOrg()).isEqualTo("WHO");
        assertThat(patient.getComments()).isEqualTo("Sordid");
        assertThat(patient.getAccountBalance()).isEqualTo(-90);
        assertThat(patient.getAddress().orElse(new Address()).getNumber()).isEqualTo("150");
        assertThat(patient.getPhone()).isEqualTo("3513221908");
        assertThat(patient.getGender()).isEqualTo("masc");
    }

    @Test
    public void findsByLastName() {

        Pageable pageable = new PageRequest(0,10);
        Page<Patient> result = repository.findByLastNameLike("Beauford", pageable);

        assertThat(result.getContent()).hasSize(1).extracting("firstName").contains("Carter");
    }

    @Test
    public void findsAllRecords() {

        Iterable<Patient> result = repository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    public void canAddMultipleCharges() {

        Patient patient = repository.findById(oliver.getIdStr());
        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extract");
        patient.addCharge(charge);
        repository.save(patient);
        charge = null;
        patient = null;

        Patient patient2 = repository.findById(oliver.getIdStr());
        Charge charge2 = new Charge(new ObjectId(), new Date(), 50, "filling");
        patient2.addCharge(charge2);
        repository.save(patient2);

        charge2 = null;
        patient2 = null;

        Patient patient3 = repository.findById(oliver.getIdStr());

        assertThat(patient3.getAccountBalance()).isEqualTo(-150);
    }

    @Test
    public void canRemoveInstallment() throws InstallmentIdNotFoundException {
        Patient patient = repository.findById(oliver.getIdStr());

        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extract");
        patient.addCharge(charge);

        Installment installment = new Installment(charge.getId(), Date.from(Instant.now()), 20);
        patient.addInstallment(installment);
        ObjectId installmentId = installment.getId();

        repository.save(patient);

        Patient patient2 = repository.findById(oliver.getIdStr());

        assertThat(patient2.getAccountBalance()).isEqualTo(-80);

        Patient patient3 = repository.findById(oliver.getIdStr());

        patient3.removeInstallment(installmentId.toString());

        repository.save(patient3);

        Patient patient4 = repository.findById(oliver.getIdStr());

        assertThat(patient4.getAccountBalance()).isEqualTo(100);
    }

    @Test
    public void canRemoveChargeWithoutInstallments() throws InstallmentPresentException {
        Patient patient = repository.findById(oliver.getIdStr());

        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extract");
        patient.addCharge(charge);

        ObjectId chargeIdToRemove = charge.getId();

        repository.save(patient);

        Patient patient2 = repository.findById(oliver.getIdStr());

        assertThat(patient2.getAccountBalance()).isEqualTo(-100);

        patient2.removeCharge(chargeIdToRemove.toString());

        repository.save(patient2);

        Patient patient3 = repository.findById(oliver.getIdStr());

        assertThat(patient3.getAccountBalance()).isZero();
    }

    @Test (expected = InstallmentPresentException.class)
    public void removingChargeWithInstallmentsThrows() throws InstallmentPresentException {
        Patient patient = repository.findById(oliver.getIdStr());

        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extract");
        patient.addCharge(charge);
        ObjectId chargeIdToRemove = charge.getId();

        Installment installment = new Installment(charge.getId(), Date.from(Instant.now()), 20);
        patient.addInstallment(installment);
        ObjectId installmentId = installment.getId();

        repository.save(patient);

        Patient patient2 = repository.findById(oliver.getIdStr());

        assertThat(patient2.getAccountBalance()).isEqualTo(-80);

        patient2.removeCharge(chargeIdToRemove.toString());

    }

//    @Test
//    public void canAddPractice() throws InvalidKeyException{
//        Patient patient = repository.findById(oliver.getIdStr());
//        Instant EXPECTED_COMPLETION_DATE = Instant.now().minus(10, ChronoUnit.DAYS);
//        int EXPECTED_PRICE = 130;
//        String PRACTICE_CODE = "01.01";
//        Practice practice = new Practice(PRACTICE_CODE, EXPECTED_COMPLETION_DATE,EXPECTED_PRICE);
//
//        patient.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).addPractice(practice);
//
//        repository.save(patient);
//
//        Patient patient2 = repository.findById(oliver.getIdStr());
//
//        Practice savedPractice = patient2.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).getPractices().get(0);
//
//        assertThat(savedPractice.getCode()).isEqualTo(PRACTICE_CODE);
//        assertThat(savedPractice.getDeliveryDate()).isEqualTo(EXPECTED_COMPLETION_DATE);
//        assertThat(savedPractice.getPrice()).isEqualTo(EXPECTED_PRICE);
//    }
//
//    @Test
//    public void canRemovePractice() throws InvalidKeyException {
//        Patient patient = repository.findById(oliver.getIdStr());
//        Instant EXPECTED_COMPLETION_DATE = Instant.now().minus(10, ChronoUnit.DAYS);
//        int EXPECTED_PRICE = 130;
//        String PRACTICE_CODE = "01.01";
//        Practice practice = new Practice(PRACTICE_CODE, EXPECTED_COMPLETION_DATE,EXPECTED_PRICE);
//
//        patient.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).addPractice(practice);
//
//        repository.save(patient);
//
//        Patient patient2 = repository.findById(oliver.getIdStr());
//        Practice practiceToRemove = patient2.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).getPractices().get(0);
//        patient2.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).removePractice(practiceToRemove);
//
//        repository.save(patient2);
//
//        Patient patient3 = repository.findById(oliver.getIdStr());
//
//        List<Practice> practices = patient3.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).getPractices();
//
//        assertThat(practices.isEmpty());
//    }
//
//    @Test
//    public void canAddMultiplePracticesToFace() throws InvalidKeyException{
//        Patient patient = repository.findById(oliver.getIdStr());
//        Instant EXPECTED_COMPLETION_DATE = Instant.now().minus(10, ChronoUnit.DAYS);
//        int EXPECTED_PRICE = 130;
//        String PRACTICE_CODE = "01.01";
//        Practice practice = new Practice(PRACTICE_CODE, EXPECTED_COMPLETION_DATE,EXPECTED_PRICE);
//        Practice practice1 = new Practice(PRACTICE_CODE, EXPECTED_COMPLETION_DATE.minus(1, ChronoUnit.DAYS), EXPECTED_PRICE);
//
//
//        patient.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).addPractice(practice);
//        patient.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).addPractice(practice1);
//
//        repository.save(patient);
//
//        Patient patient2 = repository.findById(oliver.getIdStr());
//
//        Practice savedPractice = patient2.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).getPractices().get(0);
//
//        assertThat(savedPractice.getCode()).isEqualTo(PRACTICE_CODE);
//        assertThat(savedPractice.getDeliveryDate()).isEqualTo(EXPECTED_COMPLETION_DATE);
//        assertThat(savedPractice.getPrice()).isEqualTo(EXPECTED_PRICE);
//    }

    @Test
    public void canSetADiseaseToFace(){

    }
}
