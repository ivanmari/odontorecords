package odontograme.inventory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "inventory")
public class Inventory {

    @Id
    private String id;
    private List<DentalSupply> supplies = new ArrayList<>();

    public Inventory() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DentalSupply> getSupplies() {
        return supplies;
    }

    public void setSupplies(List<DentalSupply> supplies) {
        this.supplies = supplies;
    }

    public void addOrUpdateSupply(DentalSupply supply) {
        Optional<DentalSupply> existing = supplies.stream()
                .filter(s -> s.getName() != null && s.getName().equals(supply.getName()))
                .findFirst();

        if (existing.isPresent()) {
            DentalSupply s = existing.get();
            s.setQuantity(s.getQuantity() + supply.getQuantity());
            s.setPurchaseCost(supply.getPurchaseCost()); // Update cost too?
            s.setCategory(supply.getCategory());
        } else {
            supplies.add(supply);
        }
    }

    public void consumeSupplies(List<DentalSupply> usedSupplies) {
        for (DentalSupply used : usedSupplies) {
            supplies.stream()
                    .filter(s -> s.getName() != null && s.getName().equals(used.getName()))
                    .findFirst()
                    .ifPresent(s -> s.setQuantity(s.getQuantity() - used.getQuantity()));
        }
    }
}
