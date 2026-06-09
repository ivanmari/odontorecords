package odontograme.patientrecords.odontogram;

import org.junit.Test;

import java.security.InvalidKeyException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by immari on 10/2/2016.
 */
public class MouthTests {

    @Test
    public void canGetAllPermanentTeeth() {
        Mouth mouth = new Mouth();
        try {
            Tooth centralIncisive_supDer = mouth.getTooth(1,1);
            Tooth lateralIncisive_supDer = mouth.getTooth(1,2);
            Tooth canine_supDer = mouth.getTooth(1,3);
            Tooth premol1_supDer = mouth.getTooth(1,4);
            Tooth premol2_supDer = mouth.getTooth(1,5);
            Tooth molar1_supDer = mouth.getTooth(1,6);
            Tooth molar2_supDer = mouth.getTooth(1,7);
            Tooth molar3_supDer = mouth.getTooth(1,8);

            Tooth centralIncisive_supIz = mouth.getTooth(2,1);
            Tooth lateralIncisive_supIz = mouth.getTooth(2,2);
            Tooth canine_supIz = mouth.getTooth(2,3);
            Tooth premol1_supIz = mouth.getTooth(2,4);
            Tooth premol2_supIz = mouth.getTooth(2,5);
            Tooth molar1_supIz = mouth.getTooth(2,6);
            Tooth molar2_supIz = mouth.getTooth(2,7);
            Tooth molar3_supIz = mouth.getTooth(2,8);

            Tooth centralIncisive_infIz = mouth.getTooth(3,1);
            Tooth lateralIncisive_infIz = mouth.getTooth(3,2);
            Tooth canine_infIz = mouth.getTooth(3,3);
            Tooth premol1_infIz = mouth.getTooth(3,4);
            Tooth premol2_infIz = mouth.getTooth(3,5);
            Tooth molar1_infIz = mouth.getTooth(3,6);
            Tooth molar2_infIz = mouth.getTooth(3,7);
            Tooth molar3_infIz = mouth.getTooth(3,8);

            Tooth centralIncisive_infDer = mouth.getTooth(3,1);
            Tooth lateralIncisive_infDer = mouth.getTooth(3,2);
            Tooth canine_infDer = mouth.getTooth(3,3);
            Tooth premol1_infDer = mouth.getTooth(3,4);
            Tooth premol2_infDer = mouth.getTooth(3,5);
            Tooth molar1_infDer = mouth.getTooth(3,6);
            Tooth molar2_infDer = mouth.getTooth(3,7);
            Tooth molar3_infDer = mouth.getTooth(3,8);


        } catch (InvalidKeyException ex) {
            assertThat(false);
        }
    }

//    @Test
//    public void newMouthHasAllPermanentTeethPresent() {
//        try {
//            Mouth mouth = new Mouth();
//
//            for(int i = 1; i <= 4; ++i) {
//                for(int j = 1; i <= 8; ++j){
//                    Tooth tooth = mouth.getTooth(i, j);
//                    assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Present);
//                }
//
//            }
//        } catch (InvalidKeyException ex) {
//            assertThat(false);
//        }
//    }


//    public void newMouthHasAllTemporaryTeethPresent() {
//        try {
//            Mouth mouth = new Mouth();
//
//            for(int i = 5; i <= 8; ++i) {
//                for(int j = 1; i <= 8; ++j){
//                    Tooth tooth = mouth.getTooth(i, j);
//                    assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Present);
//                }
//
//            }
//        } catch (InvalidKeyException ex) {
//            assertThat(false);
//        }
//    }

    @Test(expected = InvalidKeyException.class)
    public void invalidToothIdThrows() throws InvalidKeyException{

        Mouth mouth = new Mouth();

        Tooth invalidTooth = mouth.getTooth(0, 1);
    }

    @Test
    public void canGetTooth11() throws InvalidKeyException{
        Mouth mouth = new Mouth();

        Tooth centralIncisive_supDer = mouth.getTooth(1,1);

        assertThat(centralIncisive_supDer).isNotNull();
    }

    @Test
    public void canGetTooth36() throws InvalidKeyException{
        Mouth mouth = new Mouth();
        Tooth molar1_infIz = mouth.getTooth(3,6);

        assertThat(molar1_infIz).isNotNull();
    }

/*    @Test
    public void canListPracticesPerTooth() throws InvalidKeyException{
        Mouth mouth = new Mouth();
        Tooth molar1_infIz = mouth.getTooth(3,6);
        Tooth molar1_supIz = mouth.getTooth(1,6);

        molar1_infIz.addPractice(new Practice("10.23", Instant.parse("2016-11-14T01:49:30.524Z"), 100));
        molar1_supIz.addPractice(new Practice("01.01",Instant.parse("2016-11-14T01:49:30.524Z"), 100));


        assertThat(mouth.getPractices().size()).isEqualTo(2);
        assertThat(mouth.getPractices().get(1).getCode()).isEqualTo("10.23");

    }*/

/*    @Test
    public void canListPracticesPerToothFace() throws InvalidKeyException{
        Mouth mouth = new Mouth();
        Tooth tooth = mouth.getTooth(3,6);

        tooth.addPractice(Tooth.ToothFaceName.Distal, new Practice("10.23", Instant.parse("2016-11-14T01:49:30.524Z"), 200));
        tooth.addPractice(Tooth.ToothFaceName.Lingual, new Practice("01.01",Instant.parse("2016-11-13T01:49:30.524Z"), 100));

        assertThat(mouth.getPractices().size()).isEqualTo(2);
        assertThat(mouth.getPractices().get(0).getCode()).isEqualTo("10.23");

    }*/

    @Test
    public void temporariesCanBeRemoved()throws InvalidKeyException{
        Mouth mouth = new Mouth();

        mouth.removeTemporaries();

        Map<Integer, Tooth> temporaries = mouth.getTemporaryTeeth();

        /*This expression generates a stream from the Map<Integer, Tooth>, and for each map entry
        * it maps the tooth.getStatus value to a boolean, through the comparison
        * The reduce function, takes each preceding boolean and applies the AND operation with the current one
        * If only one tooth has a value != Removed, the result would be False and the test will fail*/
        Boolean removed = temporaries.entrySet().stream()
                                .map(entry -> entry.getValue().getStatus() == Tooth.ToothStatus.Removed)
                                .reduce(Boolean.TRUE, (a,b) -> a && b);

        assertThat(removed).isTrue();
    }

}
