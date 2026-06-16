package odontograme.service;

import odontograme.inventory.DentalSupply;
import java.util.List;

public interface InventoryService {
    void consumeSupplies(List<DentalSupply> supplies);
    void addSupply(DentalSupply supply);
    Iterable<DentalSupply> getAllSupplies();
}
