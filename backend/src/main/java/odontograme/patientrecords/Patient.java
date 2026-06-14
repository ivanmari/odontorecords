package odontograme.patientrecords;

import odontograme.service.AccountService;
import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.personaldata.Address;
import odontograme.service.PracticeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Patient {

    @Id
    private ObjectId id;

    @Transient
    private AccountService account;
    @Transient
    private PracticeService practices;

    public Patient() {
        this.id = new ObjectId();
        this.mouth = new Mouth();
        this.address = new Address();
    }
    private int dni;
    private String firstName;
    private String lastName;
    private Address address;
    private String socialSecurityOrg;
    private String socialId;
    private Instant birthday;
    private String gender;
    private String phone;

    private String comments;
    private Instant firstVisit;
    private Mouth mouth;

    //This list represents the status of each tooth at a particular point in time.
    private List<String> mouthSnapshot;


    public Patient(AccountService accountService, PracticeService practiceService) {
        this.id = new ObjectId();

        practices = practiceService;
        account = accountService;
        mouth = new Mouth();
        address = new Address();
    }

    public Patient(ObjectId id, AccountService accountService) {
        this.id = id;

        account = accountService;
        mouth = new Mouth();
        address = new Address();
    }

    public String getId() {
        return id.toString();
    }

    public String getIdStr(){
        return id.toString();
    }

    public int getDni() {
        return dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Optional<Address> getAddress() {
        return Optional.of(address);
    }

    public String getSocialSecurityOrg() {
        return socialSecurityOrg;
    }

    public String getSocialId() {
        return socialId;
    }

    public Instant getBirthday() {
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


    public void addPractice(Practice practice)
    {
        practices.addPractice(practice);
    }

    public Iterable<Practice> getPractices()
    {
        return practices.findAll();
    }

    public int getAccountBalance() {
        return account.getBalance(this.getId());
    }

    public Page<Charge> getCharges(Pageable p)
    {
        return account.getCharges(this.getId(), p);
    }

    public Page<Installment> getInstallments(Pageable p)
    {
        return account.getInstallments(this.getId(), p);
    }

    public void addCharge(Charge charge)
    {
        account.addCharge(charge);
    }

    public void removeCharge(String chargeId) throws InstallmentPresentException
    {
        account.removeCharge(account.findChargeById(chargeId));
    }

    public void addInstallment(Installment installment)
    {
        account.addInstallment(installment);
    }

    public void removeInstallment(String installmentId) throws InstallmentIdNotFoundException
    {
        account.removeInstallment(account.findInstallmentById(installmentId));
    }

    public Mouth getMouth() {
        return mouth;
    }

    public Instant getFirstVisit() {
        return firstVisit;
    }

    public void setDni(int dni) { this.dni = dni; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setSocialSecurityOrg(String socialSecurityOrg) {
        this.socialSecurityOrg = socialSecurityOrg;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setAccount(AccountService account) {
        this.account = account;
    }

    public void setPractices(PracticeService practices) {
        this.practices = practices;
    }

    public void setMouth(Mouth mouth) {
        this.mouth = mouth;
    }


    @Override
    public String toString() {
        return "Patient{" +
                "dni=" + dni +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", socialSecurityOrg='" + socialSecurityOrg + '\'' +
                ", socialId='" + socialId + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", account=" + account +
                ", comments='" + comments + '\'' +
                ", firstVisit=" + firstVisit +
                ", mouth=" + mouth +
                '}';
    }
}

