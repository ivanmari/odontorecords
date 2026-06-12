package odontograme;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
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
        String password = System.getenv("MONGODB_PASSWORD");
        if (password == null) {
            password = "";
        }
        String connectionString = "mongodb://ivanmari_db_user:" + password + "@ac-o3xoy7i-shard-00-00.uixnd5r.mongodb.net:27017,ac-o3xoy7i-shard-00-01.uixnd5r.mongodb.net:27017,ac-o3xoy7i-shard-00-02.uixnd5r.mongodb.net:27017/?ssl=true&replicaSet=atlas-eppj7b-shard-0&authSource=admin&appName=Odontorecords";
        System.out.println("Using connection string: " + connectionString.replace(password, "XXXXX"));

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        return MongoClients.create(settings);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
