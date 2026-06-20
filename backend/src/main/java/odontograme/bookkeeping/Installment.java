package odontograme.bookkeeping;

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

    public Installment() {
        this.id = new ObjectId();
    }

    public Installment(String chargeId, Date paymentDay, int amount) {
        this(chargeId, null, paymentDay, amount);
    }

    public Installment(String chargeId, String practiceId, Date paymentDay, int amount) {
        this(new ObjectId(chargeId), practiceId != null ? new ObjectId(practiceId) : null, paymentDay, amount);
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

    public void setChargeId(ObjectId chargeId) {
        this.chargeId = chargeId;
    }

    public void setChargeId(String chargeId) {
        if (chargeId != null && !chargeId.isEmpty()) {
            this.chargeId = new ObjectId(chargeId);
        }
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

    public Date getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(Date paymentDay) {
        this.paymentDay = paymentDay;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
