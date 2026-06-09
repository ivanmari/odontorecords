package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Mouth;
import odontograme.patientrecords.odontogram.Tooth;

import static odontograme.patientrecords.Practice.Code.Extraction;

/**
 * Created by immari on 5/3/2017.
 */
public class Implant extends Piece {

    public Implant(MouthGraphic mouthGraphic){
        super(mouthGraphic);

    }

    @Override
    public void accept(Practice practice, int position) {
        if(practice.getCode().compareTo(Extraction) == 0 )
        {
            mouthGraphic.setPiece(new Removed(mouthGraphic), position);
        }
    }

    @Override
    public String getPieceCode() {
        return "IM";
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public void draw() {

    }
}
