package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;

import java.util.function.Consumer;

/**
 * Created by immari on 5/2/2017.
 */
public abstract class Piece {
    protected MouthGraphic mouthGraphic;
    public Piece(MouthGraphic mouthGraphic){
        this.mouthGraphic = mouthGraphic;
    }
    public abstract void accept(Practice p, int position);
    public abstract String getPieceCode();

    /*Color can be white, blue or red, for healthy, treatment done, or planned respectively
    * This is an encoded CSV string. For those pieces with multiples subpieces,
    * like a Filling with faces, each subpiece color is written sequentially according to its enum
    * E.g. Filling in oclusal and messial treatment done, and vestibular treatment planned
    * color = "white, blue, white, red, blue"
    *
    * */
    public abstract String getColor();

    public abstract void draw();
}
