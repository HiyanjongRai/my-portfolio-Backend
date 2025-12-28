package HiyanjongTilungRai.Resume.Repository;

import HiyanjongTilungRai.Resume.Model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    // Get total visitor count
    long count();

    // Count visitors after a certain date
    long countByVisitedAtAfter(LocalDateTime date);

    // Count visitors this month (using JPQL, more compatible)
    @Query("SELECT COUNT(v) FROM Visitor v WHERE v.visitedAt >= :startOfMonth")
    long countThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth);

    // Count by country (JPQL)
    @Query("SELECT v.country, COUNT(v) FROM Visitor v WHERE v.country IS NOT NULL AND v.country <> '' GROUP BY v.country ORDER BY COUNT(v) DESC")
    List<Object[]> countByCountry();

    // Count by city (JPQL)
    @Query("SELECT v.city, v.country, COUNT(v) FROM Visitor v WHERE v.city IS NOT NULL AND v.city <> '' GROUP BY v.city, v.country ORDER BY COUNT(v) DESC")
    List<Object[]> countByCity();

    // Get recent visitors
    List<Visitor> findTop20ByOrderByVisitedAtDesc();

    // Get visitors with location data
    List<Visitor> findByLocationGrantedTrueOrderByVisitedAtDesc();

    // Count visitors with location permission granted
    long countByLocationGrantedTrue();

    // Find visitor by visitorId (for session tracking)
    Optional<Visitor> findByVisitorId(String visitorId);

    // Count unique visitors by visitorId (handle nulls)
    @Query("SELECT COUNT(DISTINCT v.visitorId) FROM Visitor v WHERE v.visitorId IS NOT NULL")
    long countUniqueVisitors();

    // Get daily visits for last 7 days (using JPQL FUNCTION for compatibility)
    @Query("SELECT FUNCTION('DATE', v.visitedAt), COUNT(v) FROM Visitor v WHERE v.visitedAt >= :sevenDaysAgo GROUP BY FUNCTION('DATE', v.visitedAt) ORDER BY FUNCTION('DATE', v.visitedAt)")
    List<Object[]> getDailyVisitsLastWeek(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
