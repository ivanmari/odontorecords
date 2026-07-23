package odontograme.service;

import odontograme.inventory.DentalSupply;
import odontograme.patientrecords.Practice;
import odontograme.repository.DentalSupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final DentalSupplyRepository dentalSupplyRepository;

    @Autowired
    public InventoryServiceImpl(DentalSupplyRepository dentalSupplyRepository) {
        this.dentalSupplyRepository = dentalSupplyRepository;
    }

    @Override
    public void consumeSupplies(List<Practice.UsedSupply> supplies) {
        for (Practice.UsedSupply supply : supplies) {
            if (supply.getDentalSupplyId() != null) {
                Optional<DentalSupply> dbSupplyOpt = dentalSupplyRepository.findById(supply.getDentalSupplyId());
                dbSupplyOpt.ifPresent(dbSupply -> {
                    int consumedUses = supply.getUses();
                    int currentUses = dbSupply.getCurrentUses();

                    currentUses -= consumedUses;

                    while (currentUses <= 0 && dbSupply.getQuantity() > 0) {
                        dbSupply.setQuantity(dbSupply.getQuantity() - 1);
                        if (dbSupply.getQuantity() > 0) {
                            currentUses += dbSupply.getUsesPerUnit();
                        } else {
                            currentUses = 0;
                        }
                    }

                    dbSupply.setCurrentUses(currentUses);
                    dentalSupplyRepository.save(dbSupply);
                });
            }
        }
    }

    @Override
    public void addSupply(DentalSupply supply) {
        if (supply.getId() != null) {
            Optional<DentalSupply> dbSupplyOpt = dentalSupplyRepository.findById(supply.getId());
            if (dbSupplyOpt.isPresent()) {
                DentalSupply dbSupply = dbSupplyOpt.get();
                dbSupply.setName(supply.getName());
                dbSupply.setCategory(supply.getCategory());
                dbSupply.setQuantity(supply.getQuantity());
                dbSupply.setPurchaseCost(supply.getPurchaseCost());

                if (supply.getUsesPerUnit() != dbSupply.getUsesPerUnit()) {
                    dbSupply.setUsesPerUnit(supply.getUsesPerUnit());
                    dbSupply.setCurrentUses(supply.getUsesPerUnit());
                } else {
                    dbSupply.setUsesPerUnit(supply.getUsesPerUnit());
                    if (supply.getCurrentUses() > 0) {
                        dbSupply.setCurrentUses(supply.getCurrentUses());
                    }
                }

                dentalSupplyRepository.save(dbSupply);
                return;
            }
        }

        if (supply.getCurrentUses() == 0 && supply.getQuantity() > 0) {
            supply.setCurrentUses(supply.getUsesPerUnit());
        }
        dentalSupplyRepository.save(supply);
    }

    @Override
    public Iterable<DentalSupply> getAllSupplies() {
        return dentalSupplyRepository.findAll();
    }
}
