package HiyanjongTilungRai.Resume.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitors")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User fingerprint or session identifier (anonymous tracking)
    @Column(length = 255)
    private String visitorId;

    // Geolocation data
    private Double latitude;
    private Double longitude;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String region;

    // IP-based fallback location
    @Column(length = 45)
    private String ipAddress;

    // Referrer information (which link they came from)
    @Column(length = 500)
    private String referrer;

    @Column(length = 500)
    private String pageUrl;

    // User agent for device/browser info
    @Column(length = 500)
    private String userAgent;

    // Timestamps
    @Column(nullable = false)
    private LocalDateTime visitedAt;

    // Track if location permission was granted
    private Boolean locationGranted = false;

    @PrePersist
    protected void onCreate() {
        this.visitedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }

    public Boolean getLocationGranted() {
        return locationGranted;
    }

    public void setLocationGranted(Boolean locationGranted) {
        this.locationGranted = locationGranted;
    }
}
