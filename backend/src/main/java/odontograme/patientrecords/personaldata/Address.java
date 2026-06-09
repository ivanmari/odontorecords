package odontograme.patientrecords.personaldata;

/**
 * Created by immari on 10/2/2016.
 */
public class Address {
    private String city;
    private String street;
    private String number;
    private String apartment;

    public Address(){
        city = "";
        street = "";
        number = "";
        apartment = "";
    }

    public Address(String city, String street, String number, String apartment) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.apartment = apartment;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getApartment() {
        return apartment;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
