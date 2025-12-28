package HiyanjongTilungRai.Resume.Dto;

import java.util.List;

public class VisitorStatsDto {
    private long totalViews;
    private long todayViews;
    private long thisMonthViews;
    private long uniqueVisitors;
    private long locationGrantedCount;
    private List<LocationCount> topCountries = new java.util.ArrayList<>();
    private List<LocationCount> topCities = new java.util.ArrayList<>();
    private List<RecentVisitor> recentVisitors = new java.util.ArrayList<>();
    private List<DailyCount> dailyVisits = new java.util.ArrayList<>();

    // Inner class for location counts
    public static class LocationCount {
        private String name;
        private String secondaryName; // for city, this would be country
        private long count;

        public LocationCount(String name, long count) {
            this.name = name;
            this.count = count;
        }

        public LocationCount(String name, String secondaryName, long count) {
            this.name = name;
            this.secondaryName = secondaryName;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSecondaryName() {
            return secondaryName;
        }

        public void setSecondaryName(String secondaryName) {
            this.secondaryName = secondaryName;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    // Inner class for recent visitors
    public static class RecentVisitor {
        private String city;
        private String country;
        private String visitedAt;
        private String pageUrl;
        private boolean locationGranted;

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

        public String getVisitedAt() {
            return visitedAt;
        }

        public void setVisitedAt(String visitedAt) {
            this.visitedAt = visitedAt;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public boolean isLocationGranted() {
            return locationGranted;
        }

        public void setLocationGranted(boolean locationGranted) {
            this.locationGranted = locationGranted;
        }
    }

    // Inner class for daily counts
    public static class DailyCount {
        private String date;
        private long count;

        public DailyCount(String date, long count) {
            this.date = date;
            this.count = count;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    // Getters and Setters
    public long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(long totalViews) {
        this.totalViews = totalViews;
    }

    public long getTodayViews() {
        return todayViews;
    }

    public void setTodayViews(long todayViews) {
        this.todayViews = todayViews;
    }

    public long getThisMonthViews() {
        return thisMonthViews;
    }

    public void setThisMonthViews(long thisMonthViews) {
        this.thisMonthViews = thisMonthViews;
    }

    public long getUniqueVisitors() {
        return uniqueVisitors;
    }

    public void setUniqueVisitors(long uniqueVisitors) {
        this.uniqueVisitors = uniqueVisitors;
    }

    public long getLocationGrantedCount() {
        return locationGrantedCount;
    }

    public void setLocationGrantedCount(long locationGrantedCount) {
        this.locationGrantedCount = locationGrantedCount;
    }

    public List<LocationCount> getTopCountries() {
        return topCountries;
    }

    public void setTopCountries(List<LocationCount> topCountries) {
        this.topCountries = topCountries;
    }

    public List<LocationCount> getTopCities() {
        return topCities;
    }

    public void setTopCities(List<LocationCount> topCities) {
        this.topCities = topCities;
    }

    public List<RecentVisitor> getRecentVisitors() {
        return recentVisitors;
    }

    public void setRecentVisitors(List<RecentVisitor> recentVisitors) {
        this.recentVisitors = recentVisitors;
    }

    public List<DailyCount> getDailyVisits() {
        return dailyVisits;
    }

    public void setDailyVisits(List<DailyCount> dailyVisits) {
        this.dailyVisits = dailyVisits;
    }
}
