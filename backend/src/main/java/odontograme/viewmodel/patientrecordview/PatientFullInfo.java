package odontograme.viewmodel.patientrecordview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import odontograme.service.AccountService;
import odontograme.patientrecords.Patient;
import odontograme.patientrecords.personaldata.Address;
import odontograme.service.PracticeService;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Optional;

/**
 * Created by immari on 12/16/2016.
 */
public class PatientFullInfo {
    @Id
    private String id;

    private int dni;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String streetNum;
    private String apartment;
    private String socialSecurityOrg;
    private String socialId;
    private String birthday;
    private String gender;
    private String phone;
    private String comments;
    private String firstVisit;


    public PatientFullInfo(){
    }

    public PatientFullInfo(Patient patient){
        this.id = patient.getIdStr();
        this.dni = patient.getDni();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.gender = patient.getGender();
        this.socialSecurityOrg = patient.getSocialSecurityOrg();
        this.socialId = patient.getSocialId();
        if(patient.getBirthday() != null) {
            this.birthday = patient.getBirthday().toString();
        }
        this.phone = patient.getPhone();

        Address address = patient.getAddress().orElse(new Address());

        this.city = address.getCity();
        this.street = address.getStreet();
        this.streetNum = address.getNumber();
        this.apartment = address.getApartment();

        this.comments = patient.getComments();

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDni(int dni) { this.dni = dni; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("socialSecOrg")
    public void setSocialSecurityOrg(String socialSecurityOrg) {
        this.socialSecurityOrg = socialSecurityOrg;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) { this.city = city;    }

    public void setStreet(String street) { this.street = street;    }

    public void setStreetNum(String streetNum) { this.streetNum = streetNum;   }

    public void setApartment(String apartment) { this.apartment = apartment;   }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setFirstVisit(String firstVisit) {
        this.firstVisit = firstVisit;
    }

    public String getId() {
        return id;
    }

    public int getDni() { return dni; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public String getApartment() {
        return apartment;
    }

    @JsonProperty("socialSecOrg")
    public String getSocialSecurityOrg() {
        return socialSecurityOrg;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getComments() {
        return comments;
    }

    public String getFirstVisit() {
        return firstVisit;
    }

    @JsonIgnore
    public Optional<Patient> getPatient(AccountService accountService, PracticeService practiceService){
        Patient patient = new Patient(accountService, practiceService);

        patient.setDni(this.dni);
        patient.setFirstName(this.firstName);
        patient.setLastName(this.lastName);
        patient.setGender(this.gender);
        patient.setSocialSecurityOrg(this.socialSecurityOrg);
        patient.setSocialId(this.socialId);
        if(this.birthday != null) {
            patient.setBirthday(Instant.parse(this.birthday));
        }
        patient.setPhone(this.phone);
        Address address = new Address();
        address.setCity(this.city);
        address.setStreet(this.street);
        address.setNumber(this.streetNum);
        address.setApartment(this.apartment);

        patient.setAddress(address);
        patient.setComments(this.getComments());

        return Optional.of(patient);
    }

    @Override
    public String toString() {
        return "PatientFullInfo{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", streetNum='" + streetNum + '\'' +
                ", apartment='" + apartment + '\'' +
                ", socialSecurityOrg='" + socialSecurityOrg + '\'' +
                ", socialId='" + socialId + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", comments='" + comments + '\'' +
                ", firstVisit='" + firstVisit + '\'' +
                '}';
    }
}
