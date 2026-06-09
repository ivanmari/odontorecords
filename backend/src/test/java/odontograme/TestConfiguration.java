package odontograme;

import odontograme.socialsecurity.PracticeCodeTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by immari on 12/29/2016.
 */
@Configuration
@ComponentScan(basePackages = "odontograme.socialsecurity")
public class TestConfiguration {
    @Bean
    public PracticeCodeTable practiceCode(){
        return new PracticeCodeTable();
    }
}
