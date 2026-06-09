package odontograme.viewmodel.patientrecordview;

import odontograme.patientrecords.Patient;
import odontograme.patientrecords.personaldata.Address;

import java.util.Optional;

/**
 * Created by immari on 10/26/2016.
 */
public class PatientBasicInfo{
    public PatientBasicInfo(Patient patient) {

        id = patient.getIdStr();
        fullName = patient.getFirstName() + " " + patient.getLastName();
        socialSec = patient.getSocialSecurityOrg();

        balance = patient.getAccountBalance();

        populateBasicAddress(patient.getAddress());

    }

    final private String id;
    private String fullName;
    private String socialSec;
    private String address;
    private int balance;

    private void populateBasicAddress(Optional<Address> a) {
        Address fullAddress = a.orElse(new Address());

        address = fullAddress.getStreet() + " " + fullAddress.getNumber() + ", " + fullAddress.getCity();
    }

    public String getFullName() {
        return fullName;
    }

    public String getSocialSec() {
        return socialSec;
    }

    public String getAddress() {
        return address;
    }

    public int getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }
}
