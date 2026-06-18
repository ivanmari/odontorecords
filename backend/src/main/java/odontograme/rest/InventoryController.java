package odontograme.rest;

import odontograme.inventory.DentalSupply;
import odontograme.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Iterable<DentalSupply> getAllSupplies() {
        return inventoryService.getAllSupplies();
    }

    @PostMapping
    public void addOrUpdateSupply(@RequestBody DentalSupply supply) {
        inventoryService.addSupply(supply);
    }
}
