package odontograme.rest;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");

        try {
            // Check MongoDB connectivity
            Document ping = mongoTemplate.executeCommand("{ping: 1}");
            if (ping.containsKey("ok") && ((Number) ping.get("ok")).doubleValue() == 1.0) {
                status.put("database", "UP");
                return ResponseEntity.ok(status);
            } else {
                status.put("database", "DOWN");
                status.put("status", "DOWN");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
            }
        } catch (Exception e) {
            status.put("database", "DOWN");
            status.put("status", "DOWN");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        }
    }
}
