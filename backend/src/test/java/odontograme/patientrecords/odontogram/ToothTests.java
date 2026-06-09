package odontograme.patientrecords.odontogram;

import odontograme.patientrecords.Disease;
import odontograme.patientrecords.Practice;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by immari on 10/12/2016.
 */
public class ToothTests {
//    @Test
//    public void byDefaultAToothIsPresent(){
//        Tooth tooth = new Tooth(3, 6);
//        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Present);
//    }
//
//    @Test
//    public void canAddDiseaseToEntireTooth(){
//        Tooth tooth = new Tooth(3, 6);
//        tooth.addDisease(new Disease(Disease.Code.Cavity, Instant.now().toString(), null));
//
//        assertThat(tooth.getDiseases().size()).isEqualTo(1);
//    }
//
//    @Test
//    public void canAddDiseaseToFace(){
//        Tooth tooth = new Tooth(3, 6);
//        ToothFace face = tooth.getFace(Tooth.ToothFaceName.Distal);
//
//        face.addDisease(new Disease(Disease.Code.Cavity, Instant.now().toString(), null));
//
//        assertThat(face.getDiseases().size()).isEqualTo(1);
//    }
//
//    @Test
//    public void canSetStatusToRemoved() {
//        Tooth tooth = new Tooth(3, 3);
//        tooth.setStatus(Tooth.ToothStatus.Removed);
//
//        assertThat(tooth.getStatus()).isEqualTo(Tooth.ToothStatus.Removed);
//    }
//
//    @Test
//    public void canGetPracticesAfterSomeDate(){
//        Tooth tooth = new Tooth(3, 3);
//
//        tooth.addPractice(new Practice("10.23", Instant.parse("2016-11-14T01:49:30.524Z"), 200));
//        tooth.addPractice(new Practice("10.22", Instant.parse("2016-10-14T01:49:30.524Z"), 200));
//        tooth.addPractice(new Practice("10.21", Instant.parse("2016-09-14T01:49:30.524Z"), 200));
//
//        List<Practice> practices = tooth.getPractices(Instant.parse("2016-10-05T01:49:30.524Z"));
//
//        assertThat(practices.size()).isEqualTo(2);
//        assertThat(practices.get(0).getCode()).isEqualTo("10.23");
//        assertThat(practices.get(1).getCode()).isEqualTo("10.22");
//    }
//
//    @Test
//    public void canAddPracticeToFaceButNotToWholeTooth(){
//        Tooth tooth = new Tooth(3, 3);
//
//        tooth.addPractice(Tooth.ToothFaceName.Distal, new Practice("10.23", Instant.parse("2016-11-14T01:49:30.524Z"), 200));
//
//        List<Practice> wholeTothPractices = tooth.getPractices();
//        List<Practice> faceTothPractices = tooth.getPractices(Tooth.ToothFaceName.Distal);
//
//        assertThat(wholeTothPractices).hasSize(0);
//        assertThat(faceTothPractices).hasSize(1);
//    }

/*    @Test
    public void canAddPracticeToFaceAndGetItAsConsolidatedPracticeForTheWholeTooth(){
        Tooth tooth = new Tooth(3, 3);

        tooth.addPractice(new Practice("02,01", Instant.parse("2016-10-14T01:49:30.524Z"),200));
        tooth.addPractice(Tooth.ToothFaceName.Distal, new Practice("10.23", Instant.parse("2016-11-14T01:49:30.524Z"), 200));

        List<Practice> practices = tooth.getConsolidatedPractices();

        assertThat(practices).hasSize(2);
    }*/
}
