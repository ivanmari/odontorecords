package odontograme;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
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
        // Allow overriding the MongoDB connection via environment:
        // - MONGODB_URI (full connection string)
        // - USE_LOCAL_MONGODB=true -> uses mongodb://localhost:27017/<db>
        String envUri = System.getenv("MONGODB_URI");
        String useLocal = System.getenv("USE_LOCAL_MONGODB");

        String connectionString;
        if (envUri != null && !envUri.isBlank()) {
            connectionString = envUri;
        } else if (useLocal != null && (useLocal.equalsIgnoreCase("true") || useLocal.equals("1"))) {
            connectionString = "mongodb://localhost:27017/" + getDatabaseName();
        } else {
            String password = System.getenv("MONGODB_PASSWORD");
            if (password == null || password.isBlank()) {
                // Fallback to local if password is missing to allow context startup
                connectionString = "mongodb://localhost:27017/" + getDatabaseName();
                return MongoClients.create(connectionString);
            }
            String encodedPassword;
            try {
                encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                encodedPassword = password;
            }
            connectionString = "mongodb://ivanmari_db_user:" + encodedPassword + "@ac-o3xoy7i-shard-00-00.uixnd5r.mongodb.net:27017,ac-o3xoy7i-shard-00-01.uixnd5r.mongodb.net:27017,ac-o3xoy7i-shard-00-02.uixnd5r.mongodb.net:27017/?ssl=true&replicaSet=atlas-eppj7b-shard-0&authSource=admin&appName=Odontorecords";
        }

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
