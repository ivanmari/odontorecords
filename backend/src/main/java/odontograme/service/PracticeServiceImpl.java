package odontograme.service;

import odontograme.bookkeeping.Charge;
import odontograme.patientrecords.Practice;
import odontograme.repository.PracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class PracticeServiceImpl implements PracticeService {

    private final PracticeRepository practiceRepository;
    private final InventoryService inventoryService;
    private final AccountService accountService;

    @Autowired
    public PracticeServiceImpl(PracticeRepository practiceRepository, InventoryService inventoryService, AccountService accountService)
    {
        this.practiceRepository = practiceRepository;
        this.inventoryService = inventoryService;
        this.accountService = accountService;
    }

    @Override
    public void addPractice(Practice practice) {
        if (practice.getDone()) {
            inventoryService.consumeSupplies(practice.getUsedSupplies());
        }
        Practice savedPractice = practiceRepository.save(practice);
        if (savedPractice != null && savedPractice.getDone() && savedPractice.getPrice() > 0) {
            createChargeForPractice(savedPractice);
        }
    }

    private void createChargeForPractice(Practice practice) {
        Charge charge = new Charge();
        charge.setPatientId(practice.getPatientId());
        charge.setPracticeId(practice.getId());
        charge.setCharge(practice.getPrice());
        Instant deliveryDate = practice.getDeliveryDate();
        if (deliveryDate == null || deliveryDate.equals(Instant.MAX)) {
            deliveryDate = Instant.now();
        }
        charge.setDeliveryDate(Date.from(deliveryDate));
        charge.setDetails(practice.getCode().toString());
        accountService.addCharge(charge);
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
            boolean shouldCreateCharge = false;
            if (oldPracticeOpt.isPresent()) {
                Practice oldPractice = oldPracticeOpt.get();
                if (!oldPractice.getDone() && p.getDone()) {
                    inventoryService.consumeSupplies(p.getUsedSupplies());
                    shouldCreateCharge = true;
                }
            } else if (p.getDone()) {
                inventoryService.consumeSupplies(p.getUsedSupplies());
                shouldCreateCharge = true;
            }
            practiceRepository.save(p);
            if (shouldCreateCharge && p.getPrice() > 0) {
                createChargeForPractice(p);
            }
        });
    }

    @Override
    public Page<Practice> findByPatientId(String patientId, Pageable pageable) {
        return practiceRepository.findByPatientId(patientId, pageable);
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
