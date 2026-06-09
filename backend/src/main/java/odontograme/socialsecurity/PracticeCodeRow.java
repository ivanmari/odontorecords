package odontograme.socialsecurity;

/**
 * Created by immari on 12/24/2016.
 */

public class PracticeCodeRow {

    private String code;
    private String description;
    private int paidBySocialSec;
    private int paidByPatient;
    private boolean colored;

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPaidBySocialSec(int paidBySocialSec) {
        this.paidBySocialSec = paidBySocialSec;
    }

    public void setPaidByPatient(int paidByPatient) {
        this.paidByPatient = paidByPatient;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getPaidBySocialSec() {
        return paidBySocialSec;
    }

    public int getPaidByPatient() {
        return paidByPatient;
    }

    public boolean isColored() { return colored; }
}
