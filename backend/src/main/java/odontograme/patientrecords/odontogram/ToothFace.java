package odontograme.patientrecords.odontogram;

/**
 * Created by immari on 10/3/2016.
 */
public class ToothFace {

    private Tooth.ToothFaceName faceName;
    private boolean filled;

    public ToothFace(Tooth.ToothFaceName faceName) {
        this.faceName = faceName;
        this.filled = false;
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
}
