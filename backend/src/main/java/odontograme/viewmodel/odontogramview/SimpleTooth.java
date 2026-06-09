package odontograme.viewmodel.odontogramview;

import odontograme.patientrecords.odontogram.Tooth;
import odontograme.patientrecords.odontogram.ToothFace;
import odontograme.socialsecurity.PracticeCodeTable;
import odontograme.viewmodel.odontogramview.piecegenerator.Piece;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by immari on 12/22/2016.
 *
 * A SimpleTooth is a graphic representation of a Tooth. It has only relevant info used in UI context.
 * Each tooth has 5 faces. Each face has a color associated with a practice and its delivery date.
 */
//public class SimpleTooth implements Piece {
//
//    /*
//    *  Constructor.
//    *  The closing and delivery dates, determine if this is a pre-existing/previous or recent/planned practice, hence red or blue.
//    */
//    @Autowired
//    public SimpleTooth(PracticeCodeTable practiceCodeTable, Tooth tooth, Instant closingDate) {
//        this.toothNumber = tooth.getToothNumber();
//
//        //Applying practices to each tooth face
//        this.faces = tooth.getFaces().stream().map(e -> new SimpleFace(e.getFaceName().name(), getFaceColor(practiceCodeTable, e, closingDate))).collect(Collectors.toList());
//    }
//
//    private String getFaceColor(PracticeCodeTable practiceCodeTable, ToothFace face, Instant closingDate){
//        String color ="white";
//
//        if(practiceCodeTable != null) {
//            if (!face.getPractices().stream()
//                    .filter(practice -> practice.getDeliveryDate().equals(closingDate) || practice.getDeliveryDate().isAfter(closingDate))
//                    .filter(practice -> practiceCodeTable.isFillingPractice(practice.getCode()))
//                    .collect(Collectors.toList()).isEmpty()) {
//                color = "blue";
//
//            } else if (!face.getPractices().stream()
//                    .filter(practice -> practice.getDeliveryDate().isBefore(closingDate))
//                    .filter(practice -> practiceCodeTable.isFillingPractice(practice.getCode()))
//                    .collect(Collectors.toList()).isEmpty()) {
//                color = "red";
//            }
//        }
//
//        return color;
//    }
//
//    public List<SimpleFace> getFaces() {
//        return faces;
//    }
//
//    public int getToothNumber() {
//        return toothNumber;
//    }
//
//    @Override
//    public String getPieceCode() {
//        return "ST";
//    }
//
//    @Override
//    public String getColor() {
//        return null;
//    }
//
//    @Override
//    public void draw() {
//
//    }
//
//    private int toothNumber;
//    private List<SimpleFace> faces;
//}
