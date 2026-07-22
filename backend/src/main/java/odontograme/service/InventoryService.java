package odontograme.service;

import odontograme.inventory.DentalSupply;
import odontograme.patientrecords.Practice;
import java.util.List;

public interface InventoryService {
    void consumeSupplies(List<Practice.UsedSupply> supplies);
    void addSupply(DentalSupply supply);
    Iterable<DentalSupply> getAllSupplies();
}
