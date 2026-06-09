package odontograme.service;

import odontograme.patientrecords.Practice;
import odontograme.repository.PracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PracticeServiceImpl implements PracticeService {

    private final PracticeRepository practiceRepository;

    @Autowired
    public PracticeServiceImpl(PracticeRepository practiceRepository)
    {
        this.practiceRepository = practiceRepository;
    }

    @Override
    public void addPractice(Practice practice) {
        practiceRepository.save(practice);
    }

    @Override
    public Optional<Practice> findPracticeById(String practiceId) {
        return practiceRepository.findById(practiceId);
    }

    @Override
    public void deletePracticeById(String practiceId) {
        practiceRepository.deleteById(practiceId);
    }

    @Override
    public void updatePractice(Optional<Practice> practice) {
        practice.ifPresent(practiceRepository::save);
    }

    @Override
    public Page<Practice> findAll(Pageable p) {
        return practiceRepository.findAll(p);
    }

    @Override
    public Iterable<Practice> findAll() {
        return practiceRepository.findAll();
    }
}
