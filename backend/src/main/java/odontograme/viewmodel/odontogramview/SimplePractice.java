package odontograme.viewmodel.odontogramview;

import odontograme.patientrecords.Practice;

/**
 * Created by immari on 12/22/2016.
 *
 * A SimplePractice has a code and a color.
 * It is constructed from a full Practice object. The color is determined by a closing date, compared with the
 * delivery date of the full practice
 *
 */
class SimplePractice {

    public SimplePractice(Practice practice, String color) {
        this.code = practice.getCode().toString();
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public String getColor() {
        return color;
    }

    private String code;
    private String color;
}
