package odontograme.repository;

import odontograme.patientrecords.Practice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeRepository extends MongoRepository<Practice, String> {
    Optional<Practice> findById(String id);
    Page<Practice> findByPatientId(String id, Pageable p);
}
