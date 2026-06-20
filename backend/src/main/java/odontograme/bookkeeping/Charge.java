package odontograme.bookkeeping;

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

    public Charge() {
        this.id = new ObjectId();
    }

    public Charge(String practiceId, Date deliveryDate, int charge, String details) {
        this(practiceId != null ? new ObjectId(practiceId) : null, deliveryDate, charge, details);
    }

    @PersistenceConstructor
    public Charge(ObjectId practiceId, Date deliveryDate, int charge, String details) {
        this.id = new ObjectId();
        this.practiceId = practiceId;
        this.deliveryDate = deliveryDate;
        this.charge = charge;
        this.details = details;
    }

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

    public void setPracticeId(ObjectId practiceId) {
        this.practiceId = practiceId;
    }

    public void setPracticeId(String practiceId) {
        if (practiceId != null && !practiceId.isEmpty()) {
            this.practiceId = new ObjectId(practiceId);
        }
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

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

}
