package com.cosmic.astrology.repository;

import com.cosmic.astrology.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
    Page<ActivityLog> findByUsername(String username, Pageable pageable);
    
    List<ActivityLog> findByUsernameOrderByTimestampDesc(String username);
    
    List<ActivityLog> findByUsernameAndTimestampAfterOrderByTimestampDesc(String username, LocalDateTime timestamp);
    
    List<ActivityLog> findByUsernameAndTimestampBetweenOrderByTimestampDesc(
        String username, LocalDateTime start, LocalDateTime end);
    
    List<ActivityLog> findByActivityTypeOrderByTimestampDesc(String activityType);
    
    List<ActivityLog> findByUsernameAndActivityTypeOrderByTimestampDesc(String username, String activityType);
    
    List<ActivityLog> findByStatusOrderByTimestampDesc(String status);
    
    List<ActivityLog> findByUsernameAndStatusOrderByTimestampDesc(String username, String status);
    
    List<ActivityLog> findBySessionIdOrderByTimestampDesc(String sessionId);
    
    Optional<ActivityLog> findFirstByUsernameOrderByTimestampDesc(String username);
    
    long countByUsername(String username);
    
    long countByUsernameAndActivityType(String username, String activityType);
    
    long countByUsernameAndStatus(String username, String status);
    
    long countByUsernameAndTimestampAfter(String username, LocalDateTime timestamp);
    
    long countByUsernameAndTimestampBetween(String username, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a.activityType, COUNT(a) FROM ActivityLog a WHERE a.username = :username GROUP BY a.activityType")
    List<Object[]> getActivityCountByType(@Param("username") String username);
    
    @Query("SELECT a.deviceType, COUNT(a) FROM ActivityLog a WHERE a.username = :username AND a.deviceType IS NOT NULL GROUP BY a.deviceType")
    List<Object[]> getActivityCountByDeviceType(@Param("username") String username);
    
    @Query("SELECT a.status, COUNT(a) FROM ActivityLog a WHERE a.username = :username GROUP BY a.status")
    List<Object[]> getActivityCountByStatus(@Param("username") String username);
    
    @Query("SELECT DATE(a.timestamp), COUNT(a) FROM ActivityLog a WHERE a.username = :username AND a.timestamp BETWEEN :start AND :end GROUP BY DATE(a.timestamp) ORDER BY DATE(a.timestamp)")
    List<Object[]> getDailyActivityCount(@Param("username") String username, 
                                         @Param("start") LocalDateTime start, 
                                         @Param("end") LocalDateTime end);
    
    @Query("SELECT HOUR(a.timestamp), COUNT(a) FROM ActivityLog a WHERE a.username = :username GROUP BY HOUR(a.timestamp) ORDER BY HOUR(a.timestamp)")
    List<Object[]> getHourlyActivityDistribution(@Param("username") String username);
    
    @Query("SELECT a.username, COUNT(a) as activityCount FROM ActivityLog a WHERE a.timestamp >= :since GROUP BY a.username ORDER BY activityCount DESC")
    List<Object[]> getMostActiveUsers(@Param("since") LocalDateTime since, Pageable pageable);
    
    @Query("SELECT a.activityType, COUNT(a) as count FROM ActivityLog a WHERE a.username = :username GROUP BY a.activityType ORDER BY count DESC")
    List<Object[]> getMostCommonActivities(@Param("username") String username, Pageable pageable);
    
    @Query("SELECT " +
           "COUNT(a) as totalActivities, " +
           "COUNT(CASE WHEN a.status = 'SUCCESS' THEN 1 END) as successfulActivities, " +
           "COUNT(CASE WHEN a.status = 'FAILED' THEN 1 END) as failedActivities, " +
           "AVG(a.durationMs) as avgDuration " +
           "FROM ActivityLog a WHERE a.username = :username")
    Object[] getActivitySummary(@Param("username") String username);
    
    @Query("SELECT " +
           "COUNT(a) as recentActivities, " +
           "COUNT(DISTINCT a.sessionId) as uniqueSessions, " +
           "COUNT(DISTINCT a.deviceType) as deviceTypes " +
           "FROM ActivityLog a WHERE a.username = :username AND a.timestamp >= :since")
    Object[] getRecentActivitySummary(@Param("username") String username, @Param("since") LocalDateTime since);
    
    @Query("SELECT DISTINCT a.username FROM ActivityLog a WHERE a.timestamp BETWEEN :start AND :end")
    List<String> findActiveUsersBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.durationMs > :threshold ORDER BY a.durationMs DESC")
    List<ActivityLog> findLongRunningActivities(@Param("threshold") Long thresholdMs, Pageable pageable);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.status IN ('FAILED', 'ERROR') AND a.timestamp BETWEEN :start AND :end ORDER BY a.timestamp DESC")
    List<ActivityLog> findFailedActivitiesBetween(@Param("start") LocalDateTime start, 
                                                  @Param("end") LocalDateTime end, 
                                                  Pageable pageable);
    
    @Query("SELECT " +
           "COUNT(CASE WHEN a.timestamp >= :currentStart THEN 1 END) as currentPeriod, " +
           "COUNT(CASE WHEN a.timestamp >= :previousStart AND a.timestamp < :currentStart THEN 1 END) as previousPeriod " +
           "FROM ActivityLog a WHERE a.username = :username AND a.timestamp >= :previousStart")
    Object[] getActivityTrends(@Param("username") String username,
                               @Param("currentStart") LocalDateTime currentStart,
                               @Param("previousStart") LocalDateTime previousStart);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.ipAddress LIKE :ipPattern ORDER BY a.timestamp DESC")
    List<ActivityLog> findByIpAddressPattern(@Param("ipPattern") String ipPattern, Pageable pageable);
    
    @Query("SELECT " +
           "DAYOFWEEK(a.timestamp) as dayOfWeek, " +
           "HOUR(a.timestamp) as hour, " +
           "COUNT(a) as activityCount " +
           "FROM ActivityLog a WHERE a.username = :username " +
           "GROUP BY DAYOFWEEK(a.timestamp), HOUR(a.timestamp)")
    List<Object[]> getActivityHeatMapData(@Param("username") String username);
    
    @Query("DELETE FROM ActivityLog a WHERE a.timestamp < :before")
    int deleteActivitiesOlderThan(@Param("before") LocalDateTime before);
    
    int deleteByUsername(String username);
    
    long countByTimestampBefore(LocalDateTime before);
    
    @Query("SELECT a FROM ActivityLog a WHERE LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY a.timestamp DESC")
    List<ActivityLog> searchByDescription(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.username = :username AND LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY a.timestamp DESC")
    List<ActivityLog> searchUserActivitiesByDescription(@Param("username") String username, 
                                                        @Param("keyword") String keyword, 
                                                        Pageable pageable);
    
    @Query("SELECT a FROM ActivityLog a WHERE JSON_EXTRACT(a.metadata, :jsonPath) IS NOT NULL ORDER BY a.timestamp DESC")
    List<ActivityLog> findWithMetadataKey(@Param("jsonPath") String jsonPath, Pageable pageable);
    
    @Query("SELECT a.ipAddress, COUNT(a) as failureCount FROM ActivityLog a " +
           "WHERE a.status = 'FAILED' AND a.timestamp >= :since " +
           "GROUP BY a.ipAddress HAVING COUNT(a) >= :threshold " +
           "ORDER BY failureCount DESC")
    List<Object[]> findSuspiciousIpAddresses(@Param("since") LocalDateTime since, 
                                             @Param("threshold") Long threshold);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.activityType IN :securityActivityTypes AND a.timestamp BETWEEN :start AND :end ORDER BY a.timestamp DESC")
    List<ActivityLog> findSecurityAuditActivities(@Param("securityActivityTypes") List<String> activityTypes,
                                                  @Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.username = :username AND a.activityType IN ('LOGIN', 'LOGOUT') ORDER BY a.timestamp DESC")
    List<ActivityLog> findLoginActivities(@Param("username") String username, Pageable pageable);
}
