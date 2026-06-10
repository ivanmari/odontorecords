package odontograme.socialsecurity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by immari on 12/25/2016.
 */
public class PracticeCodeTableTest {
    @Test
    public void isFillingPractice() throws Exception {

        final String CODE_ENTRY = new String("02.01");
        final String CODE_PRACTICE = new String("02.01");

        PracticeCodeTable practiceCodeTable = new PracticeCodeTable();

        PracticeCodeRow entry = new PracticeCodeRow();

        entry.setCode(CODE_ENTRY);
        entry.setColored(true);

        practiceCodeTable.addEntry(entry);

        assertThat(practiceCodeTable.isFillingPractice(CODE_PRACTICE)).isTrue();
    }

    @Test
    public void isNotFillingPractice() throws Exception {

        final String CODE_ENTRY = new String("02.01");
        final String CODE_PRACTICE = new String("02.01");

        PracticeCodeTable practiceCodeTable = new PracticeCodeTable();

        PracticeCodeRow entry = new PracticeCodeRow();

        entry.setCode(CODE_ENTRY);
        entry.setColored(false);

        practiceCodeTable.addEntry(entry);

        assertThat(practiceCodeTable.isFillingPractice(CODE_PRACTICE)).isFalse();
    }

}