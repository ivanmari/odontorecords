package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Tooth;

import java.util.*;

import static odontograme.patientrecords.Practice.Code.Extraction;
import static odontograme.patientrecords.Practice.Code.FillingBack;
import static odontograme.patientrecords.Practice.Code.FillingFront;

public class Filled extends Piece {

    private Map<Tooth.ToothFaceName,Tooth.ToothFaceColor> faces;

    private void paintFaces(Practice practice) {
        List<Tooth.ToothFaceName> affectedFaces = practice.getAffectedFaceNames();

        for (Tooth.ToothFaceName affectedFaceName : affectedFaces) {
            if (practice.isPlanned()) {
                this.faces.put(affectedFaceName, Tooth.ToothFaceColor.Red);
            } else {
                this.faces.put(affectedFaceName, Tooth.ToothFaceColor.Blue);
            }
        }
    }

    public Filled (MouthGraphic mouthGraphic) {
        super(mouthGraphic);
        EnumSet.allOf(Tooth.ToothFaceName.class).forEach(toothFaceNames -> faces.put(toothFaceNames, Tooth.ToothFaceColor.White));
    }

    @Override
    public void accept(Practice practice, int position) {

        if(practice.getCode().compareTo(FillingFront) == 0 || practice.getCode().compareTo(FillingBack) == 0)
        {
            paintFaces(practice);
        }
        else if(practice.getCode().compareTo(Extraction) == 0 )
        {
            mouthGraphic.setPiece(new Removed(mouthGraphic), position);
        }
    }

    @Override
    public String getPieceCode() {
        return null;
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public void draw() {

    }
}
