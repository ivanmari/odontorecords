package odontograme.repository;

import odontograme.socialsecurity.PracticeCodeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeCodesRepository extends MongoRepository<PracticeCodeTable, String> {
    Optional<PracticeCodeTable> findById(String id);
    PracticeCodeTable findByHealthProviderName(String name);
}
