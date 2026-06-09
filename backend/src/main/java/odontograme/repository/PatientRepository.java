package odontograme.repository;


import odontograme.patientrecords.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PatientRepository extends PagingAndSortingRepository<Patient, String> {

    Page<Patient> findByFirstName(String firstName, Pageable pageable);
    Page<Patient> findByLastNameLike(String lastName, Pageable pageable);
    Patient findById(String id);

}
