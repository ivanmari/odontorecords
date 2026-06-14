package odontograme.bookkeeping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;

/**
 * Created by immari on 10/2/2016.
 */
public class Charge {

    @Id
    private ObjectId id;

    private String patientId;
    private ObjectId practiceId;
    private Date deliveryDate;
    private int charge;
    private String details;

    //REST response constructor
    @JsonCreator
    public Charge(@JsonProperty("practiceId")String practiceId, @JsonProperty("deliveryDate")Date deliveryDate, @JsonProperty("charge")int charge, @JsonProperty("details")String details) {
        this(new ObjectId(practiceId), deliveryDate, charge, details);
    }

    @PersistenceConstructor
    public Charge(ObjectId practiceId, Date deliveryDate, int charge, String details) {
        this.id = new ObjectId();

        this.practiceId = practiceId;
        this.deliveryDate = deliveryDate;
        this.charge = charge;
        this.details = details;
    }

    /*
    public Charge()
    {this.id = new ObjectId();}
*/
    public ObjectId getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ObjectId getPracticeId() {
        return practiceId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public int getCharge() {
        return charge;
    }

}
