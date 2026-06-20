package odontograme.viewmodel.patientrecordview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import odontograme.patientrecords.Practice;

import java.time.Instant;

/**
 * Created by immari on 1/15/2017.
 *
 * TODO: Replace by HttpConverter String to Instant
 */
public class PracticeRest {

    private String patientId;
    private String code;
    private String deliveryDate;
    private int price;
    private String comments;
    private Boolean isPreexisting;
    private Boolean done;
    private String affectedPieces;

    public PracticeRest(){}

    public PracticeRest(Practice practice){

        this.patientId = practice.getPatientId();
        this.code = practice.getCode().toString();
        this.deliveryDate = practice.getDeliveryDate().toString();
        this.price = practice.getPrice();
        this.comments = practice.getComments();
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getPreexisting() { return isPreexisting; }

    public void setPreexisting(Boolean preexisting) { isPreexisting = preexisting; }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getAffectedPieces() {
        return affectedPieces;
    }

    public void setAffectedPieces(String affectedPieces) {
        this.affectedPieces = affectedPieces;
    }

    @JsonIgnore
    public Practice getPractice (){
        System.out.println("getPractice: deliveryDate = " + this.deliveryDate);
        Instant delivery = (this.deliveryDate != null && !this.deliveryDate.isEmpty()) ? Instant.parse(this.deliveryDate) : null;
        Practice practice = new Practice(Practice.Code.valueOf(this.code), delivery, this.price, this.isPreexisting != null ? this.isPreexisting : false);

        practice.setPatientId(this.patientId);
        practice.setComments(this.comments);
        practice.setPieces(this.affectedPieces);
        if (this.done != null) {
            practice.setDone(this.done);
        }

        return practice;
    }
}
