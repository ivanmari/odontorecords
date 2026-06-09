package odontograme.viewmodel.odontogramview.piecegenerator;

import odontograme.patientrecords.Practice;
import odontograme.socialsecurity.PracticeCodeTable;
import odontograme.patientrecords.odontogram.Mouth;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;

/**
 * Created by immari on 5/2/2017.
 *
 * MouthGraphic creates Removed, SimpleTooth, Bridge, Implant objects, according to practice code.
 * Gives priority to latest practice, i.e: Extraction practice, followed by Implant should not build Removed.
 * TODO Que pasa cuando habia un implante o corona y se cayo, vuelve a ser REMOVED sin practica asociada?
 * Hay algunas practicas que coexisten, por ej un relleno y un trat conducto
 * closingDate: dia de cierre de facturacion de la obra social. Sirve para pintar la pieza roja o azul
 */




/**
 * Created by immari on 12/22/2016.
 */
public class MouthGraphic {

    /*
    Array of dental pieces: Implant, Crown, Bridge or Simple Tooth

    SimpleTooth: Where each tooth has 5 faces. Each face has a color.

    Bridge: There should be a method to indicate the extent of a bridge, with its associated color

    This class will be populated when the client needs to paint the odontogram. Not when the client needs
    to show practice history or records.
     */

    @Autowired
    public MouthGraphic() {

        //this.permanentSimplePieces = new Piece[32];
        //this.temporarySimplePieces = new Piece[20];

        this.teeth = new HashMap<>();

        for (int quad = 1; quad <= 4; ++quad) {

            for (int i = 1; i <= 8; ++i) {
                teeth.put(quad * 10 + i, new Healthy(this));
            }
        }

        for (int quad = 5; quad <= 8; ++quad) {
            for (int i = 1; i <= 5; ++i) {
                teeth.put(quad * 10 + i, new Healthy(this));
            }
        }
    }

    public void applyPractices(Iterable<Practice> practices){
        for(Practice practice : practices){
            Integer.valueOf(practice.getToothName().get());
        }
    }

    public void setPiece(Piece piece, Integer position){
        teeth.put(position, piece);
    }

    public List<Piece> getPermanentSimplePieces() {
        return new ArrayList<>();
    }

    public List<Piece> getTemporarySimplePieces() {
        return new ArrayList<>();
    }

    //private Piece permanentSimplePieces[];
    //private Piece temporarySimplePieces[];
    private Map<Integer, Piece> teeth;

}


//public class MouthGraphic {
//
//    private Piece currentPiece = new Healthy(this);
//
//    public Piece populateMouth(odontograme.viewmodel.odontogramview.MouthGraphic mouth) {
//
//        Iterable<Practice> practices = patient.getPractices();
//
////Iterate practices and feed FSM to get into latest tooth state
//        practices.forEach(piece);
//
//        String pieceColor = "white";
//        String practiceCode = "";
//
//        while(practices.forEach();) {
//
//            practices.sort(Comparator.comparing(Practice::getDeliveryDate));
//
//            Practice latestPractice = practices.get(practices.size() - 1);
//
//            practiceCode = latestPractice.getCode();
//
//            if (latestPractice.getDeliveryDate().equals(closingDate) || latestPractice.getDeliveryDate().isAfter(closingDate)) {
//                pieceColor = "blue";
//            } else {
//                pieceColor = "red";
//            }
//        }
//
//        //Status has higher priority over practices
//        Tooth.ToothStatus toothStatus = tooth.getStatus();
//
//        if (!toothStatus.equals(Tooth.ToothStatus.Present)) {
//            practiceCode = mapStatus2PracticeCode(toothStatus);
//        }
//
//        if(!practiceCode.isEmpty()) {
//
//            switch (practiceCode) {
//                case "Extraction": {
//                    return new Removed(tooth);
//                }
//
//                case "Implant": {
//                    return new Implant(tooth);
//                }
//
//                case "Bridge": {
//                    return new Bridge(tooth, pieceColor);
//                }
//
//                case "Crown": {
//                    return new Crown(tooth, pieceColor);
//                }
//            }
//        }
//
//        return new SimpleTooth(practiceCodeTable, tooth, closingDate);
//    }
//
//    //TODO establecer mapping
//    static String mapStatus2PracticeCode(Tooth.ToothStatus toothStatus){
//        return toothStatus.name();
//    }
//}
