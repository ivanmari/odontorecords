package odontograme.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import odontograme.bookkeeping.AccountServiceMock;
import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.PracticeServicesMock;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.repository.PatientRepository;
import odontograme.repository.PracticeCodesRepository;
import odontograme.service.AccountService;
import odontograme.service.PracticeService;
import odontograme.socialsecurity.PracticeCodeRow;
import odontograme.socialsecurity.PracticeCodeTable;
import odontograme.viewmodel.patientrecordview.PatientFullInfo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by immari on 11/9/2016.
 */
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class PatientControllerTests {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private Patient patient1;
    private Patient patient2;
    private PracticeCodeTable practiceCode = new PracticeCodeTable();

    private List<Patient> patientList = new ArrayList<>();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PracticeCodesRepository practiceCodesRepository;

    private AccountService accountService = new AccountServiceMock();
    private PracticeService practiceService = new PracticeServicesMock();

        @BeforeEach
        public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.patientRepository.deleteAll();
        this.practiceCodesRepository.deleteAll();

        String patient1_json = "{\n" +
                "  \"id\": \"" + new ObjectId().toString() + "\",\n" +
                "  \"dni\": \"27890987\",\n" +
                "  \"firstName\": \"Richard\",\n" +
                "  \"lastName\": \"Kent\",\n" +
                "  \"city\": \"Brighton\",\n" +
                "  \"street\": \"Stroudley\",\n" +
                "  \"streetNum\": \"150\",\n" +
                "  \"apartment\": \"5D\",\n" +
                "  \"socialSecOrg\": \"Swiss Medical\",\n" +
                "  \"socialId\": \"123ab\",\n" +
                "  \"birthday\": \"1990-12-03T10:15:30Z\",\n" +
                "  \"gender\": \"female\",\n" +
                "  \"phone\": 13579,\n" +
                "  \"comments\": \"Space for more info\"\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();

        patient1 = (mapper.readValue(patient1_json, PatientFullInfo.class)).getPatient(accountService, practiceService).get();

        String patient2_json = "{\n" +
                "  \"id\": \"" + new ObjectId().toString() + "\",\n" +
                "  \"dni\": \"22810987\",\n" +
                "  \"firstName\": \"Michael\",\n" +
                "  \"lastName\": \"Perez\",\n" +
                "  \"city\": \"Brighton\",\n" +
                "  \"street\": \"Kings Road\",\n" +
                "  \"streetNum\": \"150\",\n" +
                "  \"apartment\": \"5D\",\n" +
                "  \"socialSecOrg\": \"Omint\",\n" +
                "  \"socialId\": \"6273b\",\n" +
                "  \"birthday\": \"1947-10-03T10:15:30Z\",\n" +
                "  \"gender\": \"male\",\n" +
                "  \"phone\": 740812345,\n" +
                "  \"comments\": \"Space for more info p2\"\n" +
                "}";

        patient2 = (mapper.readValue(patient2_json, PatientFullInfo.class)).getPatient(accountService, practiceService).get();

        patientList.add(patientRepository.save(patient1));
        patientList.add(patientRepository.save(patient2));

        PracticeCodeRow entry = new PracticeCodeRow();

        entry.setCode("02.02");
        entry.setColored(true);
        entry.setDescription("Filling back");
        entry.setPaidBySocialSec(100);
        entry.setPaidByPatient(20);


        PracticeCodeRow entry2 = new PracticeCodeRow();

        entry2.setCode("02.08");
        entry2.setColored(true);
        entry2.setDescription("Filling front");
        entry2.setPaidBySocialSec(200);
        entry2.setPaidByPatient(30);

        this.practiceCode.addEntry(entry);
        this.practiceCode.addEntry(entry2);
        this.practiceCode.setHealthProviderName("Generic");

        this.practiceCodesRepository.save(practiceCode);
    }

    @Test
    public void readExistingPatient() throws Exception {
        mockMvc.perform(get("/patient/" + patient1.getIdStr()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.patient1.getIdStr())));
    }

    @Test
    public void readListOfPatients() throws Exception {
        String output = mockMvc.perform(get("/patient/?name=" ))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(output);
    }

        @Disabled
    @Test
    public void readGraphicMouth() throws Exception {

        Practice practice = new Practice(Practice.Code.FillingFront, Instant.parse("2016-12-03T10:15:30.00Z"), 100);
       // patient1.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).addPractice(practice);
        patientRepository.save(patient1);


        String output = mockMvc.perform(get("/patient/" + patient1.getIdStr() + "/graphmouth?closingDate=2016-12-30T10:15:30.00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['permanentTeeth'][0]['toothNumber']", is(11)))
                .andExpect(jsonPath("$['permanentTeeth'][0]['faces'][0]['name']", is("Distal")))
                .andExpect(jsonPath("$['permanentTeeth'][0]['faces'][0]['color']", is("blue")))
                .andReturn().getResponse().getContentAsString();

        System.out.println(output);
    }

    @Test
    public void canAddNewPatient() throws Exception {

        final String addedPatientId = null;
        String json = "{\n" +
                "  \"id\": \"" + addedPatientId + "\",\n" +
                "  \"dni\": \"17890987\",\n" +
                "  \"firstName\": \"Joe\",\n" +
                "  \"lastName\": \"Ruiz\",\n" +
                "  \"city\": \"Southampton\",\n" +
                "  \"street\": \"Stroudley\",\n" +
                "  \"streetNum\": \"150\",\n" +
                "  \"apartment\": \"5D\",\n" +
                "  \"socialSecOrg\": \"Osde\",\n" +
                "  \"socialId\": \"123ab\",\n" +
                "  \"birthday\": \"1990-12-03T10:15:30Z\",\n" +
                "  \"gender\": \"female\",\n" +
                "  \"phone\": 12345,\n" +
                "  \"comments\": \"Space for more info\"\n" +
                "}";

        mockMvc.perform(post("/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

       mockMvc.perform(get("/patient/?name=Ruiz"))
                .andExpect(status().is(200));
               // .andExpect(jsonPath("$.Content[0]['fullName']", is("Joe Ruiz")));
        /*
                .andExpect(jsonPath("$[0]['firstName']", is("Joe")))
                .andExpect(jsonPath("$[0]['lastName']", is("Kent")))
                .andExpect(jsonPath("$[0]['city']", is("Southampton")))
                .andExpect(jsonPath("$[0]['street']", is("Stroudley")))
                .andExpect(jsonPath("$[0]['streetNum']", is("150")))
                .andExpect(jsonPath("$[0]['apartment']", is("5D")))
                .andExpect(jsonPath("$[0]['socialSecOrg']", is("Osde")))
                .andExpect(jsonPath("$[0]['socialId']", is("123ab")))
                .andExpect(jsonPath("$[0]['birthday']", is("1990-12-03T10:15:30Z")))
                .andExpect(jsonPath("$[0]['phone']", is("12345")))
                .andExpect(jsonPath("$[0]['gender']", is("female")))
                .andExpect(jsonPath("$[0]['comments']", is("Space for more info")));
               */

    }

    @Test
    public void canDeletePatient() throws Exception {
        String patientIdToDel = this.patient1.getIdStr();

        String result = mockMvc.perform(delete("/patient/" + String.valueOf(patientIdToDel)))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();

        assertThat(result.isEmpty());
    }

    @Test
    public void canModifyPatientGender() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        patient1.setGender("male");

        String patient_gender_json = mapper.writeValueAsString(new PatientFullInfo(patient1));

        System.out.println (patient_gender_json);

        mockMvc.perform(put("/patient")
                .contentType(MediaType.APPLICATION_JSON).content(patient_gender_json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/patient/" + patient1.getIdStr()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(27890987)))
                .andExpect(jsonPath("$.firstName", is("Richard")))
                .andExpect(jsonPath("$.lastName", is("Kent")))
                .andExpect(jsonPath("$.city", is("Brighton")))
                .andExpect(jsonPath("$.street", is("Stroudley")))
                .andExpect(jsonPath("$.streetNum", is("150")))
                .andExpect(jsonPath("$.apartment", is("5D")))
                .andExpect(jsonPath("$.socialSecOrg", is("Swiss Medical")))
                .andExpect(jsonPath("$.socialId", is("123ab")))
                .andExpect(jsonPath("$.birthday", is("1990-12-03T10:15:30Z")))
                .andExpect(jsonPath("$.phone", is("13579")))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.comments", is("Space for more info")));
    }

    //Accounting

    @Test
    public void canAddCharge() throws Exception{

        String charge_json = "{\n" +
                "  \"practiceId\": \"589f0b2ed5569d31b8698e3d\", \n" +
                "  \"deliveryDate\": \"1990-12-03T10:15:30.00Z\",\n" +
                "  \"charge\": 100,\n" +
                "  \"details\": null\n" +
                "}";

        System.out.println(charge_json);

        String account_url = "/patient/" + patient1.getIdStr() + "/account/";

         mockMvc.perform(post(account_url + "charges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(charge_json))
                .andExpect(status().isCreated());

         mockMvc.perform(get(account_url + "balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(-100)));
    }

    @Test
    public void canDeleteCharge() throws Exception{

        String account_url = "/patient/" + patient1.getIdStr() + "/account/";
        Charge charge = new Charge(new ObjectId(), Date.from(Instant.now()), 200, "Text with details");
        patient1.addCharge(charge);
        patientRepository.save(patient1);

        assertThat(patient1.getAccountBalance()).isEqualTo(-200);

        mockMvc.perform(delete(account_url + "charges/" + charge.getId().toString()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(account_url + "balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(0)));

    }

    @Test
    public void chargesWithInstallmentsCannotBeDeleted() throws Exception{
        String account_url = "/patient/" + patient1.getIdStr() + "/account/";

        Charge charge = new Charge(new ObjectId(), Date.from(Instant.now()), 200, "Text with details");
        patient1.addCharge(charge);

        Installment installment = new Installment(charge.getId(), Date.from(Instant.now()), 150);
        patient1.addInstallment(installment);

        assertThat(patient1.getAccountBalance()).isEqualTo(-50);

        patientRepository.save(patient1);

        //Tries to delete through REST
        mockMvc.perform(delete(account_url + "charges/" + charge.getId().toString()))
                .andExpect(status().isUnprocessableEntity());

        //Verifies that the charge is still there
        assertThat(patient1.getAccountBalance()).isEqualTo(-50);
    }


    @Test
    public void canAddInstallment() throws Exception{
        String account_url = "/patient/" + patient1.getIdStr() + "/account/";

        final String PRACTICE_ID = "589f0b2ed5569d31b8698e3d";
        Charge charge = new Charge(PRACTICE_ID, java.util.Date.from(Instant.now()), 100, "more details");

        patient1.addCharge(charge);
        patientRepository.save(patient1);

        final String INSTALLMENT_ID = "589f0b2ed5569d31b8698e3d";
        final String CHARGE_ID = charge.getId().toHexString();

        Installment dummyInstallment = new Installment(CHARGE_ID, Date.from(Instant.now()), 100);

        assertThat(dummyInstallment.getChargeId().toString()).isEqualTo(CHARGE_ID);

        String installment_json = "{\n" +
                "  \"_id\": \"" + INSTALLMENT_ID + "\", \n" +
                "  \"chargeId\": \"" + CHARGE_ID + "\", \n" +
                "  \"paymentDate\": \"1990-12-03T10:15:30.00Z\",\n" +
                "  \"amount\": 100\n" +
                "}";

        mockMvc.perform(post(account_url + "installments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(installment_json))
                .andExpect(status().isCreated());
    }

    @Test
    public void canDeleteInstallment() throws Exception{
        String account_url = "/patient/" + patient1.getIdStr() + "/account/";

        Charge charge = new Charge(new ObjectId(), Date.from(Instant.now()), 200, "Text with details");
        patient1.addCharge(charge);

        Installment installment = new Installment(charge.getId(),Date.from(Instant.now()), 100);
        patient1.addInstallment(installment);

        patientRepository.save(patient1);

        assertThat(patient1.getAccountBalance()).isEqualTo(-100);

        mockMvc.perform(delete(account_url + "installments/" + installment.getId().toString()))
               .andExpect(status().isNoContent());

        mockMvc.perform(get(account_url + "balance"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.balance", is(-200)));
    }

        @Disabled
    @Test
    public void canAddPracticeToFace() throws Exception {

        String practice_json = "{\n" +
                "  \"code\": \"02.02\",\n" +
                "  \"deliveryDate\": \"1990-12-03T10:15:30.00Z\",\n" +
                "  \"price\": 100\n" +
                "}";

        String post_url = "/patient/" + patient1.getIdStr() + "/11/Distal/practice";

        mockMvc.perform(post(post_url)
               .contentType(MediaType.APPLICATION_JSON).content(practice_json))
               .andExpect(status().isCreated());

        String get_url = "/patient/" + patient1.getIdStr() + "/11/Distal/practices";

        mockMvc.perform(get(get_url))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]['code']", is("02.02")))
               .andExpect(jsonPath("$[0]['deliveryDate']", is("1990-12-03T10:15:30Z")))
               .andExpect(jsonPath("$[0]['price']", is(100)));
    }

    @Test
    public void canDeletePracticeFromFace() throws Exception{
        Practice practice = new Practice(Practice.Code.FillingFront, Instant.parse("2016-12-03T10:15:30.00Z"), 100);
        String practiceId = practice.getId();
        //patient1.getMouth().getToothByID(11).getFace(Tooth.ToothFaceName.Distal).addPractice(practice);
        patientRepository.save(patient1);

        String delete_url = "/patient/" + patient1.getIdStr() + "/11/Distal/practice/" + practiceId;

        String result = mockMvc.perform(delete(delete_url))
                               .andExpect(status().isNoContent())
                               .andReturn().getResponse().getContentAsString();

        assertThat(result.isEmpty());
    }

    @Test
    public void canGetPracticesOfFace() throws Exception {
        Practice practice1 = new Practice(Practice.Code.FillingFront, Instant.now(), 100);
        Practice practice2 = new Practice(Practice.Code.FillingBack, Instant.now().minus(Duration.ofDays(1)), 100);

        //this.patient1.getMouth().getTooth(1, 1).addPractice(Tooth.ToothFaceName.Distal, practice1);
        //this.patient1.getMouth().getTooth(1, 1).addPractice(Tooth.ToothFaceName.Distal, practice2);

        this.patientRepository.save(patient1);

        String url = "/patient/" + patient1.getIdStr() + "/11/Distal/practices";

        mockMvc.perform(get(url))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]['code']", is("02.01")))
               .andExpect(jsonPath("$[1]['code']", is("02.02")));
    }

    @Test
    public void canGetPracticesOfTooth() throws Exception {
        Practice practice1 = new Practice(Practice.Code.FillingFront, Instant.now(), 100);
        Practice practice2 = new Practice(Practice.Code.FillingBack, Instant.now().minus(Duration.ofDays(1)), 100);

        //this.patient1.getMouth().getTooth(1, 1).addPractice(Tooth.ToothFaceName.Distal, practice1);
        //this.patient1.getMouth().getTooth(1, 1).addPractice(Tooth.ToothFaceName.Mesial, practice2);

        this.patientRepository.save(patient1);

        String url = "/patient/" + patient1.getIdStr() + "/11/practices";

        mockMvc.perform(get(url))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$['Distal'][0]['code']", is("02.01")))
               .andExpect(jsonPath("$['Mesial'][0]['code']", is("02.02")));
    }
}