package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Tooth;

import static odontograme.patientrecords.Practice.Code.Extraction;

/**
 * Created by immari on 5/3/2017.
 */
public class Bridge extends Piece {

    public Bridge(MouthGraphic mouthGraphic){
        super(mouthGraphic);
        this.color = color;
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
        return "BR";
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public void draw() {

    }

    int id;
    String color;
}
