package odontograme.inventory;

import odontograme.patientrecords.Practice;
import odontograme.repository.DentalSupplyRepository;
import odontograme.repository.PracticeRepository;
import odontograme.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private DentalSupplyRepository dentalSupplyRepository;

    @Mock
    private PracticeRepository practiceRepository;

    private InventoryService inventoryService;
    private PracticeService practiceService;
    private AccountService accountService;

    @Mock
    private odontograme.repository.ChargeRepository chargeRepository;
    @Mock
    private odontograme.repository.InstallmentRepository installmentRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryServiceImpl(dentalSupplyRepository);
        accountService = new AccountServiceImpl(chargeRepository, installmentRepository, dentalSupplyRepository);
        practiceService = new PracticeServiceImpl(practiceRepository, inventoryService, accountService);
    }

    @Test
    public void testConsumeSuppliesWithUses() {
        DentalSupply resinInDb = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 1000, 2, 10);
        resinInDb.setId("resin123");
        resinInDb.setCurrentUses(10);

        Practice.UsedSupply usedResin = new Practice.UsedSupply("resin123", 3);

        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resinInDb));

        inventoryService.consumeSupplies(Collections.singletonList(usedResin));

        assertThat(resinInDb.getQuantity()).isEqualTo(2);
        assertThat(resinInDb.getCurrentUses()).isEqualTo(7);
        verify(dentalSupplyRepository, times(1)).save(resinInDb);
    }

    @Test
    public void testConsumeSuppliesDecreasesQuantity() {
        DentalSupply resinInDb = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 1000, 2, 10);
        resinInDb.setId("resin123");
        resinInDb.setCurrentUses(2);

        Practice.UsedSupply usedResin = new Practice.UsedSupply("resin123", 3);

        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resinInDb));

        inventoryService.consumeSupplies(Collections.singletonList(usedResin));

        assertThat(resinInDb.getQuantity()).isEqualTo(1);
        assertThat(resinInDb.getCurrentUses()).isEqualTo(9);
        verify(dentalSupplyRepository, times(1)).save(resinInDb);
    }

    @Test
    public void testConsumeSuppliesDepletesCompletely() {
        DentalSupply resinInDb = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 1000, 1, 10);
        resinInDb.setId("resin123");
        resinInDb.setCurrentUses(2);

        Practice.UsedSupply usedResin = new Practice.UsedSupply("resin123", 3);

        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resinInDb));

        inventoryService.consumeSupplies(Collections.singletonList(usedResin));

        assertThat(resinInDb.getQuantity()).isEqualTo(0);
        assertThat(resinInDb.getCurrentUses()).isEqualTo(0);
        verify(dentalSupplyRepository, times(1)).save(resinInDb);
    }

    @Test
    public void testPracticeServiceTriggersConsumption() {
        DentalSupply resinInDb = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 1000, 10, 1);
        resinInDb.setId("resin123");
        resinInDb.setCurrentUses(1);
        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resinInDb));

        Practice.UsedSupply usedResin = new Practice.UsedSupply("resin123", 1);

        Practice practice = new Practice(Practice.Code.FillingBack, Instant.now(), 100);
        practice.setUsedSupplies(Collections.singletonList(usedResin));
        practice.setDone(true);
        when(practiceRepository.save(any(Practice.class))).thenReturn(practice);

        practiceService.addPractice(practice);

        assertThat(resinInDb.getQuantity()).isEqualTo(9);
        verify(dentalSupplyRepository, times(1)).save(resinInDb);
        verify(practiceRepository, times(1)).save(practice);
    }

    @Test
    public void testPracticeServiceTriggersConsumptionOnTransition() {
        DentalSupply resinInDb = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        resinInDb.setId("resin123");
        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resinInDb));

        Practice.UsedSupply usedResin = new Practice.UsedSupply("resin123", 1);

        Practice practice = new Practice(Practice.Code.FillingBack, null, 100);
        practice.setId(new org.bson.types.ObjectId().toString());
        practice.setUsedSupplies(Collections.singletonList(usedResin));
        practice.setDone(false);

        // Old practice state (planned)
        Practice oldPractice = new Practice(Practice.Code.FillingBack, null, 100);
        oldPractice.setDone(false);
        when(practiceRepository.findById(practice.getId())).thenReturn(Optional.of(oldPractice));

        // Transition to done
        practice.setDone(true);
        practiceService.updatePractice(Optional.of(practice));

        assertThat(resinInDb.getQuantity()).isEqualTo(9);
        verify(dentalSupplyRepository, times(1)).save(resinInDb);
    }

    @Test
    public void testPracticeCostWithUses() {
        DentalSupply resinInDb = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 1000, 1, 10);
        resinInDb.setId("resin123");
        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resinInDb));

        Practice.UsedSupply usedResin = new Practice.UsedSupply("resin123", 1);
        Practice practice = new Practice(Practice.Code.FillingBack, Instant.now(), 100);
        practice.setUsedSupplies(Collections.singletonList(usedResin));

        assertThat(accountService.getPracticeCost(practice)).isEqualTo(200);
    }

    @Test
    public void testAddSupplyInitializesCurrentUses() {
        DentalSupply newSupply = new DentalSupply("New Supply", DentalSupplyCategory.Resin, 500, 5, 20);
        // id is null, so it's a new supply

        inventoryService.addSupply(newSupply);

        assertThat(newSupply.getCurrentUses()).isEqualTo(20);
        verify(dentalSupplyRepository, times(1)).save(newSupply);
    }
}
