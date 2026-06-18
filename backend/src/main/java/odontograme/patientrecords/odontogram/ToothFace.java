package odontograme.patientrecords.odontogram;

/**
 * Created by immari on 10/3/2016.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ToothFace {

    private Tooth.ToothFaceName faceName;
    private boolean filled;
    private boolean planned;
    @JsonIgnore
    private java.util.List<odontograme.patientrecords.Disease> diseases;

    public ToothFace() {
        this.diseases = new java.util.ArrayList<>();
    }

    public ToothFace(Tooth.ToothFaceName faceName) {
        this.faceName = faceName;
        this.filled = false;
        this.planned = false;
        this.diseases = new java.util.ArrayList<>();
    }

    public Tooth.ToothFaceName getFaceName(){
        return faceName;
    }

    public void setFaceName(Tooth.ToothFaceName faceName) {
        this.faceName = faceName;
    }

    public void setFilled(boolean filled)
    {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    public boolean isPlanned() {
        return planned;
    }

    public void setPlanned(boolean planned) {
        this.planned = planned;
    }

    public void addDisease(odontograme.patientrecords.Disease disease){
        this.diseases.add(disease);
    }

    public java.util.List<odontograme.patientrecords.Disease> getDiseases(){
        return this.diseases;
    }
}
