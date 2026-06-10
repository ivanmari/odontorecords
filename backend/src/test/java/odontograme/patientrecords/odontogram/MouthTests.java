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
        for (int quad = 1; quad <= 4; ++quad) {
            for (int order = 1; order <= 8; ++order) {
                try {
                    assertThat(mouth.getTooth(quad, order)).isNotNull();
                } catch (InvalidKeyException ex) {
                    // should not happen
                    assertThat(false);
                }
            }
        }
    }

    @Test
    public void newMouthHasAllPermanentTeethPresent() throws InvalidKeyException {
        Mouth mouth = new Mouth();

        for(int i = 1; i <= 4; ++i) {
            for(int j = 1; j <= 8; ++j){
                Tooth tooth = mouth.getTooth(i, j);
                assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Healthy);
            }

        }
    }


    @Test
    public void newMouthHasAllTemporaryTeethPresent() throws InvalidKeyException {
        Mouth mouth = new Mouth();

        for(int i = 5; i <= 8; ++i) {
            for(int j = 1; j <= 5; ++j){
                Tooth tooth = mouth.getTooth(i, j);
                assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Healthy);
            }

        }
    }

    @Test(expected = InvalidKeyException.class)
    public void invalidToothIdThrows() throws InvalidKeyException{

        Mouth mouth = new Mouth();
        mouth.getTooth(0, 1);
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
