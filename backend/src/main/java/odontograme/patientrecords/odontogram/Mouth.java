package odontograme.patientrecords.odontogram;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import odontograme.patientrecords.Practice;

 /**
  * This class represents the mouth of a patient, with all its teeth, gums, tongue and lips. It is the main class of the odontogram package. It will not be used as the main model for the front end, as we have MouthSnapshot for that. 
  * It will be used as the main model for the database. This way we can have a full history of the mouth state over time, and so we could undo practices if neded.
  */

public class Mouth {
    private Map<Integer, Tooth> permanentTeeth;
    private Map<Integer, Tooth> temporaryTeeth;

    /**
     *  Attribute set when no more temporaries are present
     * */
    private boolean isAdult;

    //Tongue tongue;
    //Gums gums;
    //Lips

    public Mouth() {
        this.permanentTeeth = new HashMap<>();
        this.temporaryTeeth = new HashMap<>();
        this.isAdult = false;
/*
        Tooth CentralIncisive_UpperLeft = new Tooth(1, 1);
        Tooth LateralIncisive_UpperLeft = new Tooth(1, 2);
        Tooth Canine_UpperLeft = new Tooth(1, 3);
        Tooth FirstPremolar_UpperLeft = new Tooth(1, 4);
        Tooth SecondPremolar_UpperLeft = new Tooth(1, 5);
        Tooth FirstMolar_UpperLeft = new Tooth(1, 6);
        Tooth SecondMolar_UpperLeft = new Tooth(1, 7);
        Tooth ThirdMolar_UpperLeft = new Tooth(1, 8);

        Tooth CentralIncisive_UpperRight = new Tooth(2, 1);
        Tooth LateralIncisive_UpperRight = new Tooth(2, 2);
        Tooth Canine_UpperRight = new Tooth(2, 3);
        Tooth FirstPremolar_UpperRight = new Tooth(2, 4);
        Tooth SecondPremolar_UpperRight = new Tooth(2, 5);
        Tooth FirstMolar_UpperRight = new Tooth(2, 6);
        Tooth SecondMolar_UpperRight = new Tooth(2, 7);
        Tooth ThirdMolar_UpperRight = new Tooth(2, 8);

        Tooth CentralIncisive_LowerRight = new Tooth(3, 1);
        Tooth LateralIncisive_LowerRight = new Tooth(3, 2);
        Tooth Canine_LowerRight = new Tooth(3, 3);
        Tooth FirstPremolar_LowerRight = new Tooth(3, 4);
        Tooth SecondPremolar_LowerRight = new Tooth(3, 5);
        Tooth FirstMolar_LowerRight = new Tooth(3, 6);
        Tooth SecondMolar_LowerRight = new Tooth(3, 7);
        Tooth ThirdMolar_LowerRight = new Tooth(3, 8);

        Tooth CentralIncisive_LowerLeft = new Tooth(4, 1);
        Tooth LateralIncisive_LowerLeft = new Tooth(4, 2);
        Tooth Canine_LowerLeft = new Tooth(4, 3);
        Tooth FirstPremolar_LowerLeft = new Tooth(4, 4);
        Tooth SecondPremolar_LowerLeft = new Tooth(4, 5);
        Tooth FirstMolar_LowerLeft = new Tooth(4, 6);
        Tooth SecondMolar_LowerLeft = new Tooth(4, 7);
        Tooth ThirdMolar_LowerLeft = new Tooth(4, 8);
*/

/*
        for (int quad = 1; quad <= 4; ++quad) {

            teeth.put(quad * 10 + 1, new Tooth(quad, 1));
            teeth.put(quad * 10 + 2, new Incisive(quad, 2));
            teeth.put(quad * 10 + 3, new Canine((quad)));
            teeth.put(quad * 10 + 4, new Premolar(quad, 1));
            teeth.put(quad * 10 + 5, new Premolar(quad, 2));
            teeth.put(quad * 10 + 6, new Molar(quad, 1));
            teeth.put(quad * 10 + 7, new Molar(quad, 2));
            teeth.put(quad * 10 + 8, new Molar(quad, 3));
        }
*/

        for (int quad = 1; quad <= 4; ++quad) {

            for (int i = 1; i <= 8; ++i) {
                permanentTeeth.put(quad * 10 + i, new Tooth(quad, i));
            }
        }

        for (int quad = 5; quad <= 8; ++quad) {
            for (int i = 1; i <= 5; ++i) {
                temporaryTeeth.put(quad * 10 + i, new Tooth(quad, i));
            }
        }
    }

    public Tooth getToothByID(int id) throws InvalidKeyException {

        if (temporaryTeeth.containsKey(id)) {
            return temporaryTeeth.get(id);
        } else if (permanentTeeth.containsKey(id)) {
            return permanentTeeth.get(id);
        } else {
            throw new InvalidKeyException("Tooth id invalid");
        }
    }

    public Tooth getTooth(int quad, int order) throws InvalidKeyException {
        int id = quad * 10 + order;
        return getToothByID(id);
    }

    public void removeTemporaries()throws InvalidKeyException{

        for (int quad = 5; quad <= 8; ++quad) {
            for (int i = 1; i <= 5; ++i) {
                getTooth(quad, i).setStatus(Tooth.ToothStatus.Removed);
            }
        }

        this.isAdult = true;
    }


//Returns the practices done on permanent and temporary teeth*//*
    public List<Practice> getPractices(){
        List<Practice> permanentPractices = permanentTeeth.entrySet().stream()
                .flatMap(mapEntry -> mapEntry.getValue().getPractices().stream())
                .collect(Collectors.toList());

        List<Practice> temporaryPractices = temporaryTeeth.entrySet().stream()
                .flatMap(mapEntry -> mapEntry.getValue().getPractices().stream())
                .collect(Collectors.toList());

        return Stream.concat(permanentPractices.stream(), temporaryPractices.stream()).collect(Collectors.toList());
    }

    public Map<Integer, Tooth> getPermanentTeeth() {
        return permanentTeeth;
    }

    public void setPermanentTeeth(Map<Integer, Tooth> permanentTeeth) {
        this.permanentTeeth = permanentTeeth;
    }

    public Map<Integer, Tooth> getTemporaryTeeth() {
        return temporaryTeeth;
    }

    public void setTemporaryTeeth(Map<Integer, Tooth> temporaryTeeth) {
        this.temporaryTeeth = temporaryTeeth;
    }

    public boolean isAdult() {
        return isAdult;
    }
}
