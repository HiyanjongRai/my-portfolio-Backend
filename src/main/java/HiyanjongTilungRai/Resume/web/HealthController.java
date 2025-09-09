package HiyanjongTilungRai.Resume.web;// src/main/java/HiyanjongTilungRai/Resume/Web/HealthController.java

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    // Expose both; pick one in frontend/Render
    @GetMapping({"/health", "/api/health"})
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
