package odontograme.repository;

import odontograme.patientrecords.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Page<Patient> findByFirstName(String firstName, Pageable pageable);
    Page<Patient> findByLastNameLike(String lastName, Pageable pageable);
    Optional<Patient> findById(String id);

}
