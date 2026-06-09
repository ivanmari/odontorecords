package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;

/**
 * Created by immari on 5/3/2017.
 */
public class Removed extends Piece {

    public Removed(MouthGraphic mouthGraphic){
        super(mouthGraphic);
    }

    @Override
    public void accept(Practice practice, int position) {
        if(practice.getCode().compareTo(Practice.Code.Implant) == 0 )
        {
            mouthGraphic.setPiece(new Implant(mouthGraphic), position);
        }
    }

    @Override
    public String getPieceCode() {
        return "RM";
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public void draw() {

    }
}
