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
    private final InventoryService inventoryService;

    @Autowired
    public PracticeServiceImpl(PracticeRepository practiceRepository, InventoryService inventoryService)
    {
        this.practiceRepository = practiceRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    public void addPractice(Practice practice) {
        if (practice.getDone()) {
            inventoryService.consumeSupplies(practice.getUsedSupplies());
        }
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
        practice.ifPresent(p -> {
            Optional<Practice> oldPracticeOpt = practiceRepository.findById(p.getId());
            if (oldPracticeOpt.isPresent()) {
                Practice oldPractice = oldPracticeOpt.get();
                if (!oldPractice.getDone() && p.getDone()) {
                    inventoryService.consumeSupplies(p.getUsedSupplies());
                }
            } else if (p.getDone()) {
                inventoryService.consumeSupplies(p.getUsedSupplies());
            }
            practiceRepository.save(p);
        });
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
