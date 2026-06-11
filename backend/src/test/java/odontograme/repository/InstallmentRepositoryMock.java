package odontograme.repository;

import odontograme.bookkeeping.Installment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstallmentRepositoryMock implements InstallmentRepository {

    private final List<Installment> store = new ArrayList<>();

    @Override
    public Optional<Installment> findById(String id) {
        return store.stream().filter(i -> i.getId().toString().equals(id)).findFirst();
    }

    @Override
    public Page<Installment> findByPatientId(String id, Pageable p) {
        List<Installment> list = store.stream().filter(i -> id.equals(i.getPatientId())).collect(Collectors.toList());
        int start = (int) p.getOffset();
        int end = Math.min(start + p.getPageSize(), list.size());
        return new PageImpl<>(list.subList(Math.max(0, start), Math.max(start, end)), p, list.size());
    }

    @Override
    public List<Installment> findByPatientId(String id) {
        return store.stream().filter(i -> id.equals(i.getPatientId())).collect(Collectors.toList());
    }

    @Override
    public Page<Installment> findByChargeId(String chargeId, Pageable p) {
        List<Installment> list = store.stream().filter(i -> chargeId.equals(i.getChargeId().toString())).collect(Collectors.toList());
        int start = (int) p.getOffset();
        int end = Math.min(start + p.getPageSize(), list.size());
        return new PageImpl<>(list.subList(Math.max(0, start), Math.max(start, end)), p, list.size());
    }

    @Override
    public List<Installment> findByChargeId(String chargeId) {
        return store.stream().filter(i -> chargeId.equals(i.getChargeId().toString())).collect(Collectors.toList());
    }

    @Override
    public List<Installment> findAll(Sort sort) {
        return new ArrayList<>(store);
    }

    @Override
    public Page<Installment> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), store.size());
        return new PageImpl<>(store.subList(Math.max(0, start), Math.max(start, end)), pageable, store.size());
    }

    @Override
    public <S extends Installment> S save(S entity) {
        if (entity.getPatientId() == null) {
            entity.setPatientId("1982");
        }
        store.removeIf(i -> i.getId().equals(entity.getId()));
        store.add(entity);
        return entity;
    }

    @Override
    public <S extends Installment> List<S> saveAll(Iterable<S> entities) {
        List<S> added = new ArrayList<>();
        for (S e : entities) {
            save(e);
            added.add(e);
        }
        return added;
    }

    @Override
    public <S extends Installment> S insert(S entity) {
        return save(entity);
    }

    @Override
    public <S extends Installment> List<S> insert(Iterable<S> entities) {
        return saveAll(entities);
    }

    // Legacy methods adapted to modern signatures
    @Override
    public boolean existsById(String s) {
        return findById(s).isPresent();
    }

    @Override
    public List<Installment> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public List<Installment> findAllById(Iterable<String> strings) {
        List<String> ids = new ArrayList<>();
        strings.forEach(ids::add);
        return store.stream().filter(i -> ids.contains(i.getId().toString())).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void deleteById(String s) {
        store.removeIf(i -> i.getId().toString().equals(s));
    }

    @Override
    public void delete(Installment entity) {
        store.removeIf(i -> i.getId().equals(entity.getId()));
    }

    @Override
    public void deleteAll(Iterable<? extends Installment> entities) {
        entities.forEach(e -> delete(e));
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        ids.forEach(this::deleteById);
    }

    // Methods from newer repository interfaces - provide simple stubs
    @Override
    public <S extends Installment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Installment> List<S> findAll(Example<S> example) {
        return Collections.emptyList();
    }

    @Override
    public <S extends Installment> List<S> findAll(Example<S> example, Sort sort) {
        return Collections.emptyList();
    }

    @Override
    public <S extends Installment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 1), 0);
    }

    @Override
    public <S extends Installment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Installment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Object findBy(Example example, java.util.function.Function queryFunction) {
        return null;
    }

}
