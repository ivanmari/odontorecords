package odontograme.repository;

import odontograme.patientrecords.Practice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by immari on 2/4/2017.
 */
public interface PracticeRepository extends PagingAndSortingRepository<Practice, String> {
    Practice findById(int id);
    Page<Practice> findByPatientId(String id, Pageable p);
    
}

