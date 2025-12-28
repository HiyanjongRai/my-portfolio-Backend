package HiyanjongTilungRai.Resume.Services;

import HiyanjongTilungRai.Resume.Dto.VisitorDto;
import HiyanjongTilungRai.Resume.Dto.VisitorStatsDto;
import HiyanjongTilungRai.Resume.Model.Visitor;
import HiyanjongTilungRai.Resume.Repository.VisitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorService {

    private static final Logger log = LoggerFactory.getLogger(VisitorService.class);
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    /**
     * Record a new visitor
     */
    public Visitor recordVisit(VisitorDto dto, String ipAddress, String userAgent) {
        log.info("Recording visit from IP: {}, Location Granted: {}", ipAddress, dto.getLocationGranted());

        Visitor visitor = new Visitor();
        visitor.setVisitorId(dto.getVisitorId());
        visitor.setLatitude(dto.getLatitude());
        visitor.setLongitude(dto.getLongitude());
        visitor.setCity(dto.getCity());
        visitor.setCountry(dto.getCountry());
        visitor.setRegion(dto.getRegion());
        visitor.setReferrer(dto.getReferrer());
        visitor.setPageUrl(dto.getPageUrl());
        visitor.setLocationGranted(dto.getLocationGranted() != null ? dto.getLocationGranted() : false);
        visitor.setIpAddress(ipAddress);
        visitor.setUserAgent(userAgent);

        Visitor saved = visitorRepository.save(visitor);
        log.info("Visit recorded with ID: {}, City: {}, Country: {}", saved.getId(), saved.getCity(),
                saved.getCountry());
        return saved;
    }

    /**
     * Get comprehensive visitor statistics for admin dashboard
     */
    public VisitorStatsDto getVisitorStats() {
        log.info("Fetching visitor statistics...");
        VisitorStatsDto stats = new VisitorStatsDto();

        try {
            // Total views
            stats.setTotalViews(visitorRepository.count());
            log.debug("Total views: {}", stats.getTotalViews());

            // Today's views
            LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
            stats.setTodayViews(visitorRepository.countByVisitedAtAfter(startOfToday));
            log.debug("Today's views: {}", stats.getTodayViews());

            // This month's views
            LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            stats.setThisMonthViews(visitorRepository.countThisMonth(startOfMonth));
            log.debug("This month's views: {}", stats.getThisMonthViews());

            // Unique visitors
            stats.setUniqueVisitors(visitorRepository.countUniqueVisitors());
            log.debug("Unique visitors: {}", stats.getUniqueVisitors());

            // Location granted count
            stats.setLocationGrantedCount(visitorRepository.countByLocationGrantedTrue());
            log.debug("Location granted count: {}", stats.getLocationGrantedCount());

        } catch (Exception e) {
            log.error("Error fetching basic stats: {}", e.getMessage());
        }

        // Top countries
        try {
            List<Object[]> countryData = visitorRepository.countByCountry();
            List<VisitorStatsDto.LocationCount> topCountries = new ArrayList<>();
            for (int i = 0; i < Math.min(10, countryData.size()); i++) {
                Object[] row = countryData.get(i);
                String country = row[0] != null ? row[0].toString() : "Unknown";
                long count = ((Number) row[1]).longValue();
                topCountries.add(new VisitorStatsDto.LocationCount(country, count));
            }
            stats.setTopCountries(topCountries);
            log.debug("Top countries loaded: {}", topCountries.size());
        } catch (Exception e) {
            log.error("Error fetching country stats: {}", e.getMessage());
            stats.setTopCountries(new ArrayList<>());
        }

        // Top cities
        try {
            List<Object[]> cityData = visitorRepository.countByCity();
            List<VisitorStatsDto.LocationCount> topCities = new ArrayList<>();
            for (int i = 0; i < Math.min(10, cityData.size()); i++) {
                Object[] row = cityData.get(i);
                String city = row[0] != null ? row[0].toString() : "Unknown";
                String country = row[1] != null ? row[1].toString() : "";
                long count = ((Number) row[2]).longValue();
                topCities.add(new VisitorStatsDto.LocationCount(city, country, count));
            }
            stats.setTopCities(topCities);
            log.debug("Top cities loaded: {}", topCities.size());
        } catch (Exception e) {
            log.error("Error fetching city stats: {}", e.getMessage());
            stats.setTopCities(new ArrayList<>());
        }

        // Recent visitors
        try {
            List<Visitor> recentVisitors = visitorRepository.findTop20ByOrderByVisitedAtDesc();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<VisitorStatsDto.RecentVisitor> recentList = recentVisitors.stream()
                    .map(v -> {
                        VisitorStatsDto.RecentVisitor rv = new VisitorStatsDto.RecentVisitor();
                        rv.setCity(v.getCity() != null && !v.getCity().isEmpty() ? v.getCity() : "Unknown");
                        rv.setCountry(v.getCountry() != null && !v.getCountry().isEmpty() ? v.getCountry() : "Unknown");
                        rv.setVisitedAt(v.getVisitedAt() != null ? v.getVisitedAt().format(formatter) : "—");
                        rv.setPageUrl(v.getPageUrl());
                        rv.setLocationGranted(v.getLocationGranted() != null && v.getLocationGranted());
                        return rv;
                    })
                    .collect(Collectors.toList());
            stats.setRecentVisitors(recentList);
            log.debug("Recent visitors loaded: {}", recentList.size());
        } catch (Exception e) {
            log.error("Error fetching recent visitors: {}", e.getMessage());
            stats.setRecentVisitors(new ArrayList<>());
        }

        // Daily visits (last 7 days)
        try {
            LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(7).atStartOfDay();
            List<Object[]> dailyData = visitorRepository.getDailyVisitsLastWeek(sevenDaysAgo);
            List<VisitorStatsDto.DailyCount> dailyVisits = new ArrayList<>();
            for (Object[] row : dailyData) {
                String date = row[0] != null ? row[0].toString() : "Unknown";
                long count = ((Number) row[1]).longValue();
                dailyVisits.add(new VisitorStatsDto.DailyCount(date, count));
            }
            stats.setDailyVisits(dailyVisits);
            log.debug("Daily visits loaded: {}", dailyVisits.size());
        } catch (Exception e) {
            log.error("Error fetching daily visits: {}", e.getMessage());
            stats.setDailyVisits(new ArrayList<>());
        }

        log.info("Visitor stats fetched successfully. Total: {}, Today: {}", stats.getTotalViews(),
                stats.getTodayViews());
        return stats;
    }

    /**
     * Get total view count (for simple API)
     */
    public long getTotalViews() {
        return visitorRepository.count();
    }
}
