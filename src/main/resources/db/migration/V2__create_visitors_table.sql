-- Visitors Table for Tracking Site Views and Geolocation
-- Run this SQL in your PostgreSQL/MySQL database

CREATE TABLE IF NOT EXISTS visitors (
    id BIGSERIAL PRIMARY KEY,
    visitor_id VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    city VARCHAR(100),
    country VARCHAR(100),
    region VARCHAR(100),
    ip_address VARCHAR(45),
    referrer VARCHAR(500),
    page_url VARCHAR(500),
    user_agent VARCHAR(500),
    visited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    location_granted BOOLEAN DEFAULT FALSE
);

-- Indexes for faster queries
CREATE INDEX IF NOT EXISTS idx_visitors_visited_at ON visitors(visited_at DESC);
CREATE INDEX IF NOT EXISTS idx_visitors_visitor_id ON visitors(visitor_id);
CREATE INDEX IF NOT EXISTS idx_visitors_country ON visitors(country);
CREATE INDEX IF NOT EXISTS idx_visitors_city ON visitors(city);
CREATE INDEX IF NOT EXISTS idx_visitors_location_granted ON visitors(location_granted);

-- Comments for documentation
COMMENT ON TABLE visitors IS 'Tracks site visits including geolocation data';
COMMENT ON COLUMN visitors.visitor_id IS 'Unique anonymous identifier for each visitor session';
COMMENT ON COLUMN visitors.latitude IS 'GPS latitude from browser geolocation';
COMMENT ON COLUMN visitors.longitude IS 'GPS longitude from browser geolocation';
COMMENT ON COLUMN visitors.city IS 'City name from reverse geocoding';
COMMENT ON COLUMN visitors.country IS 'Country name from reverse geocoding';
COMMENT ON COLUMN visitors.region IS 'State/Region from reverse geocoding';
COMMENT ON COLUMN visitors.ip_address IS 'Visitor IP address (for fallback location)';
COMMENT ON COLUMN visitors.referrer IS 'Referring URL (where they came from)';
COMMENT ON COLUMN visitors.page_url IS 'Page URL visited';
COMMENT ON COLUMN visitors.user_agent IS 'Browser user agent string';
COMMENT ON COLUMN visitors.visited_at IS 'Timestamp of the visit';
COMMENT ON COLUMN visitors.location_granted IS 'Whether user granted location permission';
