package odontograme.service;

import odontograme.inventory.DentalSupply;
import odontograme.inventory.Inventory;
import odontograme.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    private Inventory getGlobalInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        if (inventories.isEmpty()) {
            Inventory newInventory = new Inventory();
            return inventoryRepository.save(newInventory);
        }
        return inventories.get(0);
    }

    @Override
    public void consumeSupplies(List<DentalSupply> supplies) {
        Inventory inventory = getGlobalInventory();
        inventory.consumeSupplies(supplies);
        inventoryRepository.save(inventory);
    }

    @Override
    public void addSupply(DentalSupply supply) {
        Inventory inventory = getGlobalInventory();
        inventory.addOrUpdateSupply(supply);
        inventoryRepository.save(inventory);
    }

    @Override
    public Iterable<DentalSupply> getAllSupplies() {
        Inventory inventory = getGlobalInventory();
        return inventory.getSupplies();
    }
}
