package odontograme.patientrecords;

import odontograme.patientrecords.personaldata.Address;
import odontograme.service.AccountService;
import odontograme.service.PracticeService;

public class PatientBuilder {

    private Patient patient;

    public PatientBuilder(AccountService accountService, PracticeService practiceService)
    {
        patient = new Patient(accountService, practiceService);
    }

    public PatientBuilder withName(String name)
    {
        patient.setFirstName(name);
        return this;
    }

    public PatientBuilder withLastName(String lastName)
    {
        patient.setLastName(lastName);
        return this;
    }

    public PatientBuilder withAddress(Address address)
    {
        patient.setAddress(address);
        return this;
    }

    public PatientBuilder withGender(String gender)
    {
        patient.setGender(gender);
        return this;
    }

    public Patient build()
    {
        return patient;
    }
}
