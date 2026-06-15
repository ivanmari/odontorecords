package odontograme.service;

import odontograme.inventory.DentalSupply;
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
    public void consumeSupplies(List<DentalSupply> supplies) {
        for (DentalSupply supply : supplies) {
            if (supply.getId() != null) {
                Optional<DentalSupply> dbSupplyOpt = dentalSupplyRepository.findById(supply.getId());
                dbSupplyOpt.ifPresent(dbSupply -> {
                    dbSupply.setQuantity(dbSupply.getQuantity() - 1);
                    dentalSupplyRepository.save(dbSupply);
                });
            }
        }
    }

    @Override
    public void addSupply(DentalSupply supply) {
        dentalSupplyRepository.save(supply);
    }

    @Override
    public Iterable<DentalSupply> getAllSupplies() {
        return dentalSupplyRepository.findAll();
    }
}
