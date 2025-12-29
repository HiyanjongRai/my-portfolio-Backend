package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Dto.VisitorDto;
import HiyanjongTilungRai.Resume.Dto.VisitorStatsDto;
import HiyanjongTilungRai.Resume.Model.Visitor;
import HiyanjongTilungRai.Resume.Services.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * Record a new visitor (public endpoint)
     */
    @PostMapping("/track")
    public ResponseEntity<?> trackVisit(@RequestBody VisitorDto dto, HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            Visitor visitor = visitorService.recordVisit(dto, ipAddress, userAgent);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Visit recorded",
                    "visitorId", visitor.getId()));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Could not record visit"));
        }
    }

    /**
     * Get total view count (public endpoint)
     */
    @GetMapping("/count")
    public ResponseEntity<?> getViewCount() {
        long count = visitorService.getTotalViews();
        return ResponseEntity.ok(Map.of("totalViews", count));
    }

    /**
     * Test endpoint (public) to verify controller availability
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("message", "Visitor controller is active"));
    }

    /**
     * Get comprehensive visitor statistics (admin only)
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitorStatsDto> getVisitorStats() {
        VisitorStatsDto stats = visitorService.getVisitorStats();
        return ResponseEntity.ok(stats);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED"
        };
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                if (ip.contains(","))
                    ip = ip.split(",")[0].trim();
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
