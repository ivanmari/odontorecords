package odontograme.repository;

import odontograme.bookkeeping.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface ChargeRepository extends PagingAndSortingRepository<Charge, String> {
    Optional<Charge> findById(String id);
    Page<Charge> findByPatientId(String id, Pageable p);
    Iterable<Charge> findByPatientId(String id);
}
