package odontograme.repository;

import odontograme.bookkeeping.Installment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class InstallmentRepositoryMock implements InstallmentRepository {
    @Override
    public Optional<Installment> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Page<Installment> findByPatientId(String id, Pageable p) {
        return null;
    }

    @Override
    public Iterable<Installment> findByPatientId(String id) {
        return null;
    }

    @Override
    public Page<Installment> findByChargeId(String chargeId, Pageable p) {
        return null;
    }

    @Override
    public Iterable<Installment> findByChargeId(String chargeId) {
        return null;
    }

    @Override
    public Iterable<Installment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Installment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Installment> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Installment> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Installment findOne(String s) {
        return null;
    }

    @Override
    public boolean exists(String s) {
        return false;
    }

    @Override
    public Iterable<Installment> findAll() {
        return null;
    }

    @Override
    public Iterable<Installment> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(Installment entity) {

    }

    @Override
    public void delete(Iterable<? extends Installment> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
