//package odontograme.viewmodel.odontogramview;
//
//import odontograme.patientrecords.Practice;
//import odontograme.patientrecords.odontogram.Tooth;
//import odontograme.patientrecords.odontogram.ToothFace;
//import odontograme.socialsecurity.PracticeCodeRow;
//import odontograme.socialsecurity.PracticeCodeTable;
//import org.junit.Before;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.time.Instant;
///**
// * Created by immari on 12/23/2016.
// */
//@ContextConfiguration(classes = TestConfiguration.class)
//public class SimpleFaceTest {
//
//    private ToothFace toothFaceRecentlyFilled;
//    private ToothFace toothFacePreviouslyFilled;
//    private ToothFace toothFaceWithPreexistingFilling;
//    private ToothFace toothFaceUntouched;
//    private PracticeCodeTable practiceCode;
//    private static Instant COLORED_PRACTICE_DATE = Instant.parse("2016-12-03T10:15:30.00Z");
//    private static Instant NOT_COLORED_PRACTIOCE_DATE = Instant.parse("2016-10-03T10:15:30.00Z");
//    private static String COLORED_PRACTICE = "02,01";
//    private static String NOT_COLORED_PRACTICE = "01,01";
//
//    @Before
//    public void setup() {
//        this.toothFaceRecentlyFilled = new ToothFace(Tooth.ToothFaceName.Distal);
//        this.toothFaceRecentlyFilled.addPractice(new Practice(Practice.Code.FillingBack, COLORED_PRACTICE_DATE, 100));
//        this.toothFaceRecentlyFilled.addPractice(new Practice(Practice.Code.FillingBack, NOT_COLORED_PRACTIOCE_DATE, 200));
//
//        this.toothFaceWithPreexistingFilling = new ToothFace(Tooth.ToothFaceName.Distal);
//        this.toothFaceWithPreexistingFilling.addPractice(new Practice(Practice.Code.FillingBack, COLORED_PRACTICE_DATE, 100, true));
//
//        this.toothFacePreviouslyFilled = new ToothFace(Tooth.ToothFaceName.Distal);
//        this.toothFacePreviouslyFilled.addPractice(new Practice(Practice.Code.FillingBack, NOT_COLORED_PRACTIOCE_DATE, 100));
//        this.toothFacePreviouslyFilled.addPractice(new Practice(Practice.Code.FillingBack, COLORED_PRACTICE_DATE, 200));
//
//        this.toothFaceUntouched = new ToothFace(Tooth.ToothFaceName.Distal);
//        this.practiceCode = new PracticeCodeTable();
//
//        PracticeCodeRow entry = new PracticeCodeRow();
//
//        entry.setCode(NOT_COLORED_PRACTICE);
//        entry.setColored(false);
//        entry.setDescription("Filling");
//        entry.setPaidBySocialSec(100);
//        entry.setPaidByPatient(20);
//
//
//        PracticeCodeRow entry2 = new PracticeCodeRow();
//
//        entry2.setCode(COLORED_PRACTICE);
//        entry2.setColored(true);
//        entry2.setDescription("FillingBetter");
//        entry2.setPaidBySocialSec(200);
//        entry2.setPaidByPatient(30);
//
//        this.practiceCode.addEntry(entry);
//        this.practiceCode.addEntry(entry2);
//    }
/*
    @Test
    public void faceWithoutPracticesIsWhite() {
        SimpleFace simpleFace = new SimpleFace(practiceCode, this.toothFaceUntouched, COLORED_PRACTICE_DATE.plus(Duration.ofDays(1)));
        assertThat(simpleFace.getColor()).isEqualTo("white");
    }

    @Test
    public void faceWithPreexistingPracticeIsRed() {
        SimpleFace simpleFace = new SimpleFace(practiceCode, this.toothFaceWithPreexistingFilling, COLORED_PRACTICE_DATE.minus(Duration.ofDays(1)));
        assertThat(simpleFace.getColor()).isEqualTo("red");
    }

    @Test
    public void faceWithPracticeDoneAfterClosingDateIsRed() {
        SimpleFace simpleFace = new SimpleFace(practiceCode, this.toothFaceRecentlyFilled, COLORED_PRACTICE_DATE.minus(Duration.ofDays(1)));
        assertThat(simpleFace.getColor()).isEqualTo("red");
    }

    @Test
    public void faceWithPracticeDoneBeforeClosingDateIsBlue() {
        SimpleFace simpleFace = new SimpleFace(practiceCode, this.toothFacePreviouslyFilled, COLORED_PRACTICE_DATE.plus(Duration.ofDays(1)));
        assertThat(simpleFace.getColor()).isEqualTo("blue");
    }

    @Test
    public void faceWithPracticeDoneOnClosingDateIsRed() {
        SimpleFace simpleFace = new SimpleFace(practiceCode, this.toothFacePreviouslyFilled,  COLORED_PRACTICE_DATE);
        assertThat(simpleFace.getColor()).isEqualTo("red");
    }

    @Test
    public void nullPracticeTableCodeGivesWhite(){
        SimpleFace simpleFace = new SimpleFace(null, this.toothFacePreviouslyFilled, COLORED_PRACTICE_DATE.plus(Duration.ofDays(1)));
        assertThat(simpleFace.getColor()).isEqualTo("white");
    }

    @Test
    public void notExistingPracticeGivesWhite(){

        ToothFace toothFace = new ToothFace(Tooth.ToothFaceName.Distal);
        toothFace.addPractice(new Practice("NOT_EXISTING", COLORED_PRACTICE_DATE, 100));
        SimpleFace simpleFace = new SimpleFace(this.practiceCode, toothFace, COLORED_PRACTICE_DATE.plus(Duration.ofDays(1)));

        assertThat(simpleFace.getColor()).isEqualTo("white");
    }

*/


//}