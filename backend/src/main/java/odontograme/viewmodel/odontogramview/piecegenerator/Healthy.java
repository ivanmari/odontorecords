package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;

import static odontograme.patientrecords.Practice.Code.Extraction;
import static odontograme.patientrecords.Practice.Code.FillingBack;
import static odontograme.patientrecords.Practice.Code.FillingFront;

public class Healthy extends Piece  {

    public Healthy(MouthGraphic mouthGraphic){
        super(mouthGraphic);
    }

    @Override
    public void accept(Practice practice, int position) {

        if(practice.getCode().compareTo(FillingFront) == 0 || practice.getCode().compareTo(FillingBack) == 0)
        {
            //Set the corresponding faces
            mouthGraphic.setPiece(new Filled(mouthGraphic), position);
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
