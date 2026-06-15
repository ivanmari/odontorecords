package odontograme.patientrecords;

import odontograme.inventory.DentalSupply;
import odontograme.patientrecords.odontogram.Tooth;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by immari on 10/2/2016.
 *
 *  This class represents a dental Practice done to a patient.
 *  A dental Practice can be classified according to delivery status as follows: Preexistent, Previous, Current or Planned.
 *  Pre-existent Practice: done before registering the patient, no delivery date.
 *  Previous Practice: done after registering the patient and invoiced to Social Security.
 *  Current Practice: done after registering the patient, but not invoiced to Social Security.
 *  Planned Practice: Not yet done but planned, so it has no delivery date.
 *
 *  Affected pieces could be one(filling) or more(bridge) They are represented as a string encoded as follows:
 *
 *      {piece number ISO 3950 notation} [letters faces affected separated by :]
 *
 *
 *  Multiple pieces are separated by commas
 *  Face names are defined by the first letter of each enumerator in ToothFaceName enum
 *
 *  E.g.
 *      Affected pieces encoded: "11 o:d, 23 m:l:v"
 *
 *      Affected pieces decoded:
 *      tooth 11 oclusal and distal faces
 *      tooth 23 mesial, lingual and vestibular faces
 *
 *
 */
@Document
public class Practice {

    public enum Code{
        FillingFront,
        FillingBack,
        RootCanal,
        Extraction,
        Implant,
        Crown,
        Bridge,
        Cleaning
    }

    @Id
    private ObjectId id;

    private String patientId;

    @Field("practice_code")
    private Practice.Code code;

    @Field("affected_pieces")
    private String affectedPieces;

    @Field("delivery_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant deliveryDate;

    @Field("price")
    private int price;

    @Field("comments")
    private String comments;

    @Field("used_supplies")
    private List<DentalSupply> usedSupplies = new ArrayList<>();

    /*This flag indicates if the practice was done before being registered as a patient*/
    private Boolean preexisting;

    /*This flag indicates if the practice was actually done, to differentiate Current from Planned.*/
    private Boolean done;

    public Practice(Practice.Code code, Instant deliveryDate, int price, boolean preexisting){
        this.id = new ObjectId();

        this.code = code;
        this.preexisting = preexisting;

        if(preexisting){
            this.deliveryDate = Instant.EPOCH;
            this.price = 0;
            this.done = false;
        }
        else {
            if (deliveryDate != null) {
                //Depending on the deliveryDate, Practice can be Previous or Current.
                this.deliveryDate = deliveryDate;
                this.done = true;
            } else {
                //Practice is planned for the future.
                this.deliveryDate = Instant.MAX;
                this.done = false;
            }

            this.price = price;
        }
    }

    public Practice(Practice.Code code, Instant deliveryDate, int price){
        this.id = new ObjectId();

        this.code = code;
        this.deliveryDate = deliveryDate;
        this.price = price;
        this.preexisting = false;
        this.done = true;
    }

    public Practice(){
        this.id = new ObjectId();
        this.preexisting = false;
        this.done = false;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }

    public Practice.Code getCode() {
        return code;
    }

    public  Instant getDeliveryDate(){
        return deliveryDate;
    }

    public  int getPrice(){
        return price;
    }

    public String getComments() {
        return comments;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<DentalSupply> getUsedSupplies() {
        return usedSupplies;
    }

    public void setUsedSupplies(List<DentalSupply> usedSupplies) {
        this.usedSupplies = usedSupplies;
    }

    public int getSuppliesCost() {
        return usedSupplies.stream().mapToInt(DentalSupply::getPurchaseCost).sum();
    }

    public Boolean isPreexisting(){
        return preexisting;
    }

    public Boolean getPreexisting() {
        return preexisting;
    }

    public void setPreexisting(Boolean preexisting) {
        this.preexisting = preexisting;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean isPlanned() { return !done;}

    public String getPieces() {
        return affectedPieces;
    }

    public List<Tooth.ToothFaceName> getAffectedFaceNames(){

        List<Tooth.ToothFaceName> faces = new ArrayList<>();
        String facesStr = "";
        Pattern pattern = Pattern.compile("^\\d\\d (.*$)");
        Matcher matcher = pattern.matcher(this.affectedPieces);

        if (matcher.find())
        {
            facesStr = matcher.group(1);
        }

        for(String face : facesStr.split(":")){
            switch (face.toUpperCase()){
                case "O":
                    faces.add(Tooth.ToothFaceName.Oclusal);
                    break;
                case "M":
                    faces.add(Tooth.ToothFaceName.Mesial);
                    break;
                case "D":
                    faces.add(Tooth.ToothFaceName.Distal);
                    break;
                case "L":
                    faces.add(Tooth.ToothFaceName.Lingual);
                    break;
                case "V":
                    faces.add(Tooth.ToothFaceName.Vestibular);
                    break;
            }
        }

        return faces;
    }

    //TODO Take into account that a Bridge has more than one piece affected, so this should return a list
    public Optional<String> getToothName() {
        Optional toothName = Optional.empty();
        Pattern pattern = Pattern.compile("^\\d\\d");
        Matcher matcher = pattern.matcher(this.affectedPieces);
        if (matcher.find())
        {
            toothName = Optional.of(matcher.group(0));
        }
        return toothName;
    }

    public void setPieces(String pieces) {
        this.affectedPieces = pieces;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
