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
public class Installment {
    @Id
    private ObjectId id;
    private String patientId;
    private ObjectId chargeId;
    private ObjectId practiceId;
    private Date paymentDay;
    private int amount;

    //REST response
    //TODO Remove this annotation as Java 8 support metadata for arguments in bytecode
    //Remember to add dependency parameter-module
    @JsonCreator
    public Installment(@JsonProperty("chargeId")String chargeId, @JsonProperty("practiceId")String practiceId, @JsonProperty("paymentDay")Date paymentDay, @JsonProperty("amount")int amount){
        this(new ObjectId(chargeId), practiceId != null ? new ObjectId(practiceId) : null, paymentDay, amount);
    }

    public Installment(String chargeId, Date paymentDay, int amount) {
        this(new ObjectId(chargeId), null, paymentDay, amount);
    }

    public Installment(ObjectId chargeId, Date paymentDay, int amount) {
        this(chargeId, null, paymentDay, amount);
    }

    @PersistenceConstructor
    public Installment(ObjectId chargeId, ObjectId practiceId, Date paymentDay, int amount) {
        this.id = new ObjectId();
        this.chargeId = chargeId;
        this.practiceId = practiceId;
        this.paymentDay = paymentDay;
        this.amount = amount;
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

    public ObjectId getChargeId() {
        return chargeId;
    }

    public ObjectId getPracticeId() {
        return practiceId;
    }

    public Date getPaymentDay() {
        return paymentDay;
    }

    public int getAmount() {
        return amount;
    }
}
