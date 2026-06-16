package odontograme.inventory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dental_supplies")
public class DentalSupply {

    @Id
    private String id;
    private String name;
    private DentalSupplyCategory category;
    private int purchaseCost;
    private int quantity;

    public DentalSupply() {}

    public DentalSupply(String name, DentalSupplyCategory category, int purchaseCost, int quantity) {
        this.name = name;
        this.category = category;
        this.purchaseCost = purchaseCost;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DentalSupplyCategory getCategory() {
        return category;
    }

    public void setCategory(DentalSupplyCategory category) {
        this.category = category;
    }

    public int getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(int purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
