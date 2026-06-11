package odontograme.repository;

import odontograme.bookkeeping.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChargeRepositoryMock implements ChargeRepository {

    private final List<Charge> store = new ArrayList<>();

    @Override
    public Optional<Charge> findById(String id) {
        return store.stream().filter(c -> c.getId().toString().equals(id)).findFirst();
    }

    @Override
    public Page<Charge> findByPatientId(String id, Pageable p) {
        List<Charge> list = store.stream().filter(c -> id.equals(c.getPatientId())).collect(Collectors.toList());
        int start = (int) p.getOffset();
        int end = Math.min(start + p.getPageSize(), list.size());
        return new PageImpl<>(list.subList(Math.max(0, start), Math.max(start, end)), p, list.size());
    }

    @Override
    public List<Charge> findByPatientId(String id) {
        return store.stream().filter(c -> id.equals(c.getPatientId())).collect(Collectors.toList());
    }

    @Override
    public List<Charge> findAll(Sort sort) {
        return new ArrayList<>(store);
    }

    @Override
    public Page<Charge> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), store.size());
        return new PageImpl<>(store.subList(Math.max(0, start), Math.max(start, end)), pageable, store.size());
    }

    @Override
    public <S extends Charge> S save(S entity) {
        if (entity.getPatientId() == null) {
            entity.setPatientId("1982");
        }
        store.removeIf(c -> c.getId().equals(entity.getId()));
        store.add(entity);
        return entity;
    }

    @Override
    public <S extends Charge> List<S> saveAll(Iterable<S> entities) {
        List<S> added = new ArrayList<>();
        for (S e : entities) {
            save(e);
            added.add(e);
        }
        return added;
    }

    @Override
    public <S extends Charge> S insert(S entity) {
        return save(entity);
    }

    @Override
    public <S extends Charge> List<S> insert(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public boolean existsById(String s) {
        return findById(s).isPresent();
    }

    @Override
    public List<Charge> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public List<Charge> findAllById(Iterable<String> strings) {
        List<String> ids = new ArrayList<>();
        strings.forEach(ids::add);
        return store.stream().filter(c -> ids.contains(c.getId().toString())).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void deleteById(String s) {
        store.removeIf(c -> c.getId().toString().equals(s));
    }

    @Override
    public void delete(Charge entity) {
        store.removeIf(c -> c.getId().equals(entity.getId()));
    }

    @Override
    public void deleteAll(Iterable<? extends Charge> entities) {
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

    // Example-based method stubs
    @Override
    public <S extends Charge> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Charge> List<S> findAll(Example<S> example) {
        return Collections.emptyList();
    }

    @Override
    public <S extends Charge> List<S> findAll(Example<S> example, Sort sort) {
        return Collections.emptyList();
    }

    @Override
    public <S extends Charge> Page<S> findAll(Example<S> example, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public <S extends Charge> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Charge> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Object findBy(Example example, java.util.function.Function queryFunction) {
        return null;
    }

}
