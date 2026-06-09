package odontograme.viewmodel.odontogramview;

import odontograme.patientrecords.Practice;
import odontograme.patientrecords.odontogram.Tooth;
import odontograme.socialsecurity.PracticeCodeRow;
import odontograme.socialsecurity.PracticeCodeTable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by immari on 12/23/2016.
 */


public class SimpleToothTest {

    final private String CODE_COLORED_01 = "02,01";
    final private String CODE_NOT_COLORED = "01,01";
    final private String CODE_COLORED_02 = "02,03";

    final private Instant INITIAL_DATE = Instant.parse("2016-10-03T10:15:30.00Z");
    final private Instant CODE_COLORED_01_DATE = INITIAL_DATE.plus(Duration.ofDays(1));
    final private Instant CODE_NOT_COLORED_DATE = INITIAL_DATE.plus(Duration.ofDays(10));
    final private Instant CODE_COLORED_02_DATE = INITIAL_DATE.plus(Duration.ofDays(15));

    private Tooth tooth;

    @Autowired
    private PracticeCodeTable practiceCodeTable;

    @Before
    public void setup(){

        this.practiceCodeTable = new PracticeCodeTable();

        PracticeCodeRow row01 = new PracticeCodeRow();
        row01.setCode(CODE_COLORED_01);
        row01.setColored(true);

        PracticeCodeRow row02 = new PracticeCodeRow();
        row02.setCode(CODE_NOT_COLORED);
        row02.setColored(false);

        PracticeCodeRow row03 = new PracticeCodeRow();
        row03.setCode(CODE_COLORED_02);
        row03.setColored(true);

        this.practiceCodeTable.addEntry(row01);
        this.practiceCodeTable.addEntry(row02);
        this.practiceCodeTable.addEntry(row03);

        tooth = new Tooth(1,1);

        List<Practice> practices = new ArrayList<>();
        practices.add(new Practice(Practice.Code.FillingBack, CODE_COLORED_01_DATE, 100));
        practices.add(new Practice(Practice.Code.FillingBack, CODE_NOT_COLORED_DATE, 150));
        practices.add(new Practice(Practice.Code.FillingBack, CODE_COLORED_02_DATE, 200));


    }
/*
    @Test
    public void practicesAfterClosingDateAreRed() throws Exception {

        SimpleTooth simpleTooth = new SimpleTooth(practiceCodeTable, tooth, INITIAL_DATE.plus(Duration.ofDays(7)));

        List<SimplePractice> simplePractices = simpleTooth.getPractices();

        assertThat(simplePractices).hasSize(1);
        assertThat(simplePractices.get(0).getColor()).isEqualTo("red");
    }

    @Test
    public void practicesBeforeClosingDateAreBlue() throws Exception {

        SimpleTooth simpleTooth = new SimpleTooth(practiceCodeTable, tooth, INITIAL_DATE.plus(Duration.ofDays(20)));
        List<SimplePractice> simplePractices = simpleTooth.getPractices();

        assertThat(simplePractices).hasSize(2);
        assertThat(simplePractices.get(0).getColor()).isEqualTo("blue");

    }
*/
    @Test
    public void simpleToothHasSameIdOfTooth() throws Exception{
//        SimpleTooth simpleTooth = new SimpleTooth(practiceCodeTable, tooth, Instant.parse("2016-11-03T10:15:30.00Z"));
//
//        assertThat(simpleTooth.getToothNumber()).isEqualTo(tooth.getToothNumber());
    }


}