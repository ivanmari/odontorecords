package odontograme.patientrecords.odontogram;

import odontograme.bookkeeping.AccountServiceMock;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.PatientBuilder;
import odontograme.patientrecords.personaldata.Address;
import odontograme.service.AccountService;
import odontograme.service.PracticeService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by immari on 10/6/2016.
 */
public class PatientTests {

    private AccountService accountService = new AccountServiceMock();
    private PracticeService practiceService = new PracticeServicesMock();

    @Test
    public void patientGetsNameSet() {
        Patient patient = new PatientBuilder(accountService,practiceService).withName("Dave").withLastName("Mathews").build();

        assertThat(patient.getFirstName()).isEqualTo("Dave");
        assertThat(patient.getLastName()).isEqualTo("Mathews");
    }

    @Test
    public void patientGetsAddressSet(){

        Address address = new Address("Cordoba", "Illia", "123", "A");
        Patient patient = new PatientBuilder(accountService, practiceService).withAddress(address).build();

        Address expected = patient.getAddress().get();

        assertThat(address.getCity()).isEqualTo("Cordoba");
        assertThat(address.getStreet()).isEqualTo("Illia");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getApartment()).isEqualTo("A");

    }

    @Test
    public void can_set_gender(){
        Patient patient = new PatientBuilder(accountService, practiceService).withGender("male").build();

        patient.setGender("male");

        assertThat(patient.getGender()).isEqualTo("male");
    }

    @Test
    public void patientHasMouth(){
        Patient patient = new PatientBuilder(accountService, practiceService).build();

        assertThat(patient.getMouth()).isNotNull();
    }
}
