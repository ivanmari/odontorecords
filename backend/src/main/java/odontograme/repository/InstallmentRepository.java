package odontograme.repository;

import odontograme.bookkeeping.Installment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface InstallmentRepository extends PagingAndSortingRepository<Installment, String> {
    Optional<Installment>   findById(String id);
    Page<Installment> findByPatientId(String id, Pageable p);
    Iterable<Installment> findByPatientId(String id);
    Page<Installment> findByChargeId(String chargeId, Pageable p);
    Iterable<Installment> findByChargeId(String chargeId);
}
