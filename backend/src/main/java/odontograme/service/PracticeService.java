package odontograme.service;

import odontograme.patientrecords.Practice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;

import java.util.Optional;

public interface PracticeService {
    void addPractice(Practice practice);
    Page<Practice> findAll(Pageable pageable);
    Iterable<Practice> findAll();
    Optional<Practice> findPracticeById(String practiceId);
    void deletePracticeById(String practiceId);
    void updatePractice(Optional<Practice> practice);

    Page<Practice> findByPatientId(String patientId, Pageable pageable);

}
