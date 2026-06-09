package odontograme.patientrecords.odontogram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by immari on 10/2/2016.
 */

public class Tooth {


    /*
      Status is forced by user and may not be associated with a practice.
      e.g.: Missing tooth extracted previously
      TODO Ojo ver si pueden coexistir, y relacion con pre-existing practice y undo
     */
    public enum ToothStatus{
        Healthy,
        Caries,
        Removed,
        Bridge,
        Crown,
        Implant
    }

    public enum ToothName {
        CentralIncisive_UpperLeft(1, 1),
        LateralIncisive_UpperLeft(1, 2),
        Canine_UpperLeft(1, 3),
        FirstPremolar_UpperLeft(1, 4),
        SecondPremolar_UpperLeft(1, 5),
        FirstMolar_UpperLeft(1, 6),
        SecondMolar_UpperLeft(1, 7),
        ThirdMolar_UpperLeft(1, 8),

        CentralIncisive_UpperRight(2, 1),
        LateralIncisive_UpperRight(2, 2),
        Canine_UpperRight(2, 3),
        FirstPremolar_UpperRight(2, 4),
        SecondPremolar_UpperRight(2, 5),
        FirstMolar_UpperRight(2, 6),
        SecondMolar_UpperRight(2, 7),
        ThirdMolar_UpperRight(2, 8),

        CentralIncisive_LowerRight(3, 1),
        LateralIncisive_LowerRight(3, 2),
        Canine_LowerRight(3, 3),
        FirstPremolar_LowerRight(3, 4),
        SecondPremolar_LowerRight(3, 5),
        FirstMolar_LowerRight(3, 6),
        SecondMolar_LowerRight(3, 7),
        ThirdMolar_LowerRight(3, 8),

        CentralIncisive_LowerLeft(4, 1),
        LateralIncisive_LowerLeft(4, 2),
        Canine_LowerLeft(4, 3),
        FirstPremolar_LowerLeft(4, 4),
        SecondPremolar_LowerLeft(4, 5),
        FirstMolar_LowerLeft(4, 6),
        SecondMolar_LowerLeft(4, 7),
        ThirdMolar_LowerLeft(4, 8);

        ToothName(int quadrant, int order) {
            this.quadrant = quadrant;
            this.order = order;
        }

        int toothCode()
        {
            return quadrant*10 + order;
        }

        public int getQuadrant() {
            return quadrant;
        }

        public int getOrder() {
            return order;
        }

        private int quadrant;
        private int order;
    }

    public enum ToothFaceName {
        Distal,
        Mesial,
        Lingual,    //palatina
        Vestibular, //labial
        Oclusal     //incisal
    }

    public enum ToothFaceColor{
        White,
        Blue,
        Red
    }

    private int quadrant;
    private int order;
    private List<ToothFace> faces;
    private ToothStatus status;

    public Tooth(){
        faces = new ArrayList<>();
    }

    public Tooth(int quadrant, int order) {

        this.quadrant = quadrant;
        this.order = order;

        faces = new ArrayList<>();
        for(ToothFaceName faceName : ToothFaceName.values() ){
            faces.add(new ToothFace(faceName));
        }

        status = ToothStatus.Healthy;
    }

    public int getQuadrant() {
        return quadrant;
    }

    public int getToothNumber()
    {
        return quadrant*10 + order;
    }

    public ToothFace getFace(ToothFaceName faceName) {
        for(ToothFace f : faces){
            if(faceName.equals(f.getFaceName())){
                return f;
            }
        }

        throw new IndexOutOfBoundsException();

    }

    public List<ToothFace> getFaces() {
        return faces;
    }

    public ToothStatus getStatus() {
        return status;
    }

    public void setStatus(ToothStatus status) {
        this.status = status;
    }

    public void setQuadrant(int quadrant) {
        this.quadrant = quadrant;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setFaces(List<ToothFace> faces) {
        this.faces = faces;
    }
}
