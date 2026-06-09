package odontograme.service;

import odontograme.patientrecords.Practice;
import odontograme.repository.PracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class PracticeServiceImpl implements PracticeService {

    PracticeRepository practiceRepository;

    @Autowired
    PracticeServiceImpl(PracticeRepository practiceRepository)
    {
        this.practiceRepository = practiceRepository;
    }

    @Override
    public void addPractice(Practice practice) {
        practiceRepository.save(practice);
    }

    @Override
    public Optional<Practice> findPracticeById(String practiceId) {
        return Optional.empty();
    }

    @Override
    public void deletePracticeById(String practiceId) {

    }

    @Override
    public void updatePractice(Optional<Practice> practice) {

    }

    @Override
    public Page<Practice> findAll(Pageable p) {
        return practiceRepository.findAll(p);
    }

    @Override
    public Iterable<Practice> findAll() {
        return null;
    }
}





