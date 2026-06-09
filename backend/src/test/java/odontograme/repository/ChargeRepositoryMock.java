package odontograme.repository;

import odontograme.bookkeeping.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class ChargeRepositoryMock implements ChargeRepository {


    @Override
    public Optional<Charge> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Page<Charge> findByPatientId(String id, Pageable p) {
        return null;
    }

    @Override
    public Iterable<Charge> findByPatientId(String id) {
        return null;
    }

    @Override
    public Iterable<Charge> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Charge> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Charge> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Charge> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Charge findOne(String s) {
        return null;
    }

    @Override
    public boolean exists(String s) {
        return false;
    }

    @Override
    public Iterable<Charge> findAll() {
        return null;
    }

    @Override
    public Iterable<Charge> findAll(Iterable<String> strings) {
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
    public void delete(Charge entity) {

    }

    @Override
    public void delete(Iterable<? extends Charge> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
