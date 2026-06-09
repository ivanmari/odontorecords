package odontograme;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Updated for modern Spring Data MongoDB (Spring Boot 3.x compatible)
 */
@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "dental_records";
    }

    @Override
    public MongoClient mongoClient() {
        // In a real modernization, this would likely use connection strings from application.properties
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
