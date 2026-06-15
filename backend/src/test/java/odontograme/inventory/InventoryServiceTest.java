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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryServiceImpl(dentalSupplyRepository);
        practiceService = new PracticeServiceImpl(practiceRepository, inventoryService);
        accountService = new AccountServiceImpl(null, null); // repositories not needed for cost calculation
    }

    @Test
    public void testConsumeSupplies() {
        DentalSupply resin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        resin.setId("resin123");

        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resin));

        inventoryService.consumeSupplies(Collections.singletonList(resin));

        assertThat(resin.getQuantity()).isEqualTo(9);
        verify(dentalSupplyRepository, times(1)).save(resin);
    }

    @Test
    public void testPracticeServiceTriggersConsumption() {
        DentalSupply resin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        resin.setId("resin123");
        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resin));

        Practice practice = new Practice(Practice.Code.FillingBack, Instant.now(), 100);
        practice.setUsedSupplies(Collections.singletonList(resin));
        practice.setDone(true);

        practiceService.addPractice(practice);

        assertThat(resin.getQuantity()).isEqualTo(9);
        verify(dentalSupplyRepository, times(1)).save(resin);
        verify(practiceRepository, times(1)).save(practice);
    }

    @Test
    public void testPracticeServiceTriggersConsumptionOnTransition() {
        DentalSupply resin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        resin.setId("resin123");
        when(dentalSupplyRepository.findById("resin123")).thenReturn(Optional.of(resin));

        Practice practice = new Practice(Practice.Code.FillingBack, null, 100);
        practice.setId(new org.bson.types.ObjectId().toString());
        practice.setUsedSupplies(Collections.singletonList(resin));
        practice.setDone(false);

        // Old practice state (planned)
        Practice oldPractice = new Practice(Practice.Code.FillingBack, null, 100);
        oldPractice.setDone(false);
        when(practiceRepository.findById(practice.getId())).thenReturn(Optional.of(oldPractice));

        // Transition to done
        practice.setDone(true);
        practiceService.updatePractice(Optional.of(practice));

        assertThat(resin.getQuantity()).isEqualTo(9);
        verify(dentalSupplyRepository, times(1)).save(resin);
    }

    @Test
    public void testPracticeCost() {
        DentalSupply resin = new DentalSupply("Test Resin", DentalSupplyCategory.Resin, 50, 10);
        Practice practice = new Practice(Practice.Code.FillingBack, Instant.now(), 100);
        practice.setUsedSupplies(Collections.singletonList(resin));

        assertThat(practice.getSuppliesCost()).isEqualTo(50);
        assertThat(accountService.getPracticeCost(practice)).isEqualTo(50);
    }
}
