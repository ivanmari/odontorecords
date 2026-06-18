package odontograme.rest;

import odontograme.SecurityConfig;
import odontograme.inventory.DentalSupply;
import odontograme.inventory.DentalSupplyCategory;
import odontograme.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
@Import(SecurityConfig.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    public void testGetAllSupplies() throws Exception {
        DentalSupply supply1 = new DentalSupply("Resin A", DentalSupplyCategory.Resin, 100, 10);
        DentalSupply supply2 = new DentalSupply("Anesthesia B", DentalSupplyCategory.Anesthesia, 50, 20);

        when(inventoryService.getAllSupplies()).thenReturn(Arrays.asList(supply1, supply2));

        mockMvc.perform(get("/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Resin A"))
                .andExpect(jsonPath("$[1].name").value("Anesthesia B"));
    }

    @Test
    public void testAddOrUpdateSupply() throws Exception {
        mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Resin\",\"category\":\"Resin\",\"purchaseCost\":120,\"quantity\":5}"))
                .andExpect(status().isOk());

        verify(inventoryService, times(1)).addSupply(any(DentalSupply.class));
    }
}
