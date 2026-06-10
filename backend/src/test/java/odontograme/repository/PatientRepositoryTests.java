package odontograme.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PatientRepositoryTests {

    @Autowired
    private PatientRepository repository;

    @Test
    public void basicCrudSmoke() {
        // simple smoke test to ensure test sources compile and repository wiring works
        long count = repository.count();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }
}
