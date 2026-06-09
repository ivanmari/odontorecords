package odontograme.patientrecords;

import java.util.List;

/**
 * Created by immari on 10/3/2016.
 */
public class Disease {
    public enum Code{
        Cavity,
        Twist
    }
    private Code code;
    private List<Practice> associatedPractices;
    private String diagnosisDate;

    public Disease(Code code, String diagnosisDate, List<Practice> associatedPractices){
        this.code = code;
        this.diagnosisDate = diagnosisDate;
        this.associatedPractices = associatedPractices;
    }

    public Code getCode() {
        return code;
    }

    public List<Practice> getAssociatedPractices() {
        return associatedPractices;
    }

    public String getDiagnosisDate() {
        return diagnosisDate;
    }
}
