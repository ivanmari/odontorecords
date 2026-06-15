package odontograme.repository;

import odontograme.inventory.DentalSupply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentalSupplyRepository extends MongoRepository<DentalSupply, String> {
}
