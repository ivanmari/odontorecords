package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Tooth;

import static odontograme.patientrecords.Practice.Code.Extraction;

/**
 * Created by immari on 5/4/2017.
 */
public class Crown extends Piece {

    public Crown(MouthGraphic mouthGraphic){
        super(mouthGraphic);

    }

    @Override
    public void accept(Practice practice, int position) {
        if(practice.getCode().compareTo(Extraction) == 0 )
        {
            mouthGraphic.setPiece(new Removed(mouthGraphic), position);
        }
        else if(practice.getCode().compareTo(Practice.Code.Implant) == 0 )
        {
            mouthGraphic.setPiece(new Implant(mouthGraphic), position);
        }
    }

    @Override
    public String getPieceCode() {
        return "CR";
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void draw() {

    }

    int id;
    String color;
}
