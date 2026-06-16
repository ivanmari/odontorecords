package odontograme.inventory;

import odontograme.patientrecords.Practice;
import odontograme.repository.InventoryRepository;
import odontograme.repository.PracticeRepository;
import odontograme.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private PracticeRepository practiceRepository;

    private InventoryService inventoryService;
    private PracticeService practiceService;
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryServiceImpl(inventoryRepository);
        practiceService = new PracticeServiceImpl(practiceRepository, inventoryService);
        accountService = new AccountServiceImpl(null, null);

        // Setup a global inventory mock
        Inventory inventory = new Inventory();
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory);
        when(inventoryRepository.findAll()).thenReturn(inventories);
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void testConsumeSuppliesMultipleQuantity() {
        Inventory inventory = inventoryRepository.findAll().get(0);
        DentalSupply resinInInv = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        inventory.getSupplies().add(resinInInv);

        DentalSupply usedResin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 3);

        inventoryService.consumeSupplies(Collections.singletonList(usedResin));

        assertThat(resinInInv.getQuantity()).isEqualTo(7);
        verify(inventoryRepository, atLeastOnce()).save(inventory);
    }

    @Test
    public void testPracticeServiceTriggersConsumption() {
        Inventory inventory = inventoryRepository.findAll().get(0);
        DentalSupply resinInInv = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        inventory.getSupplies().add(resinInInv);

        DentalSupply usedResin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 1);

        Practice practice = new Practice(Practice.Code.FillingBack, Instant.now(), 100);
        practice.setUsedSupplies(Collections.singletonList(usedResin));
        practice.setDone(true);

        practiceService.addPractice(practice);

        assertThat(resinInInv.getQuantity()).isEqualTo(9);
        verify(inventoryRepository, atLeastOnce()).save(inventory);
        verify(practiceRepository, times(1)).save(practice);
    }

    @Test
    public void testPracticeCostMultipleQuantity() {
        DentalSupply resin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 2);
        Practice practice = new Practice(Practice.Code.FillingBack, Instant.now(), 100);
        practice.setUsedSupplies(Collections.singletonList(resin));

        assertThat(practice.getSuppliesCost()).isEqualTo(100);
        assertThat(accountService.getPracticeCost(practice)).isEqualTo(100);
    }
}
