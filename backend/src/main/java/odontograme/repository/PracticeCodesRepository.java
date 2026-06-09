package odontograme.repository;

import odontograme.socialsecurity.PracticeCodeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by immari on 12/24/2016.
 */
//TODO por que no usar repo de rows y agregarle un campo de prestador? Tabla inherente a la base
public interface PracticeCodesRepository extends PagingAndSortingRepository<PracticeCodeTable, String> {
    PracticeCodeTable findById(int id);
    PracticeCodeTable findByHealthProviderName(String name);
}
