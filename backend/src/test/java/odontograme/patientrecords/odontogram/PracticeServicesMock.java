package odontograme.patientrecords.odontogram;

import odontograme.patientrecords.Practice;
import odontograme.service.PracticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class PracticeServicesMock implements PracticeService {
    @Override
    public void addPractice(Practice practice) {

    }

    @Override
    public Page<Practice> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<Practice> findAll() {
        return null;
    }

    @Override
    public Optional<Practice> findPracticeById(String practiceId) {
        return Optional.empty();
    }

    @Override
    public void deletePracticeById(String practiceId) {

    }

    @Override
    public void updatePractice(Optional<Practice> practice) {

    }

    @Override
    public Page<Practice> findByPatientId(String patientId, Pageable pageable) {
        return null;
    }
}
