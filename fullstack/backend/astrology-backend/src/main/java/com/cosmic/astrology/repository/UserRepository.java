package com.cosmic.astrology.repository;

import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameIgnoreCase(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailIgnoreCase(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByUsernameIgnoreCase(String username);
    
    Boolean existsByEmail(String email);
    
    Boolean existsByEmailIgnoreCase(String email);
    
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:identifier) OR LOWER(u.email) = LOWER(:identifier)")
    Optional<User> findByUsernameOrEmailIgnoreCase(@Param("identifier") String identifier);
    
    List<User> findByRole(UserRole role);
    
    Page<User> findByRole(UserRole role, Pageable pageable);
    
    List<User> findByEnabledTrue();
    
    List<User> findByEnabledFalse();
    
    List<User> findByRoleAndEnabledTrue(UserRole role);
    
    List<User> findByEmailVerifiedTrue();
    
    List<User> findByEmailVerifiedFalse();
    
    List<User> findByEmailVerifiedAndEnabled(Boolean emailVerified, Boolean enabled);
    
    @Query("SELECT u FROM User u WHERE u.birthDateTime IS NOT NULL AND u.birthLatitude IS NOT NULL AND u.birthLongitude IS NOT NULL AND u.birthLocation IS NOT NULL")
    List<User> findUsersWithCompleteBirthData();
    
    @Query("SELECT u FROM User u WHERE u.birthDateTime IS NOT NULL AND u.birthLatitude IS NOT NULL AND u.birthLongitude IS NOT NULL AND u.birthLocation IS NOT NULL")
    Page<User> findUsersWithCompleteBirthData(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.birthDateTime IS NULL OR u.birthLatitude IS NULL OR u.birthLongitude IS NULL OR u.birthLocation IS NULL")
    List<User> findUsersWithIncompleteBirthData();
    
    List<User> findByChartCalculatedTrue();
    
    List<User> findByChartCalculatedFalse();
    
    List<User> findByChartCalculatedAtAfter(LocalDateTime date);
    
    List<User> findBySunSign(String sunSign);
    
    List<User> findByMoonSign(String moonSign);
    
    List<User> findByRisingSign(String risingSign);
    
    List<User> findByDominantElement(String dominantElement);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:sunSign IS NULL OR u.sunSign = :sunSign) AND " +
           "(:moonSign IS NULL OR u.moonSign = :moonSign) AND " +
           "(:risingSign IS NULL OR u.risingSign = :risingSign) AND " +
           "(:dominantElement IS NULL OR u.dominantElement = :dominantElement)")
    List<User> findByAstrologicalCriteria(
        @Param("sunSign") String sunSign,
        @Param("moonSign") String moonSign,
        @Param("risingSign") String risingSign,
        @Param("dominantElement") String dominantElement
    );
    
    List<User> findByLastLoginAfter(LocalDateTime date);
    
    List<User> findByLastLoginBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT u FROM User u WHERE u.lastLogin < :cutoffDate OR u.lastLogin IS NULL")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    List<User> findByLoginStreakGreaterThan(Integer streak);
    
    List<User> findByChartsGeneratedGreaterThan(Integer count);
    
    @Query("SELECT u FROM User u WHERE u.chartsGenerated > 0 ORDER BY u.chartsGenerated DESC")
    List<User> findMostActiveUsers(Pageable pageable);
    
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT u FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    List<User> findUsersCreatedToday();
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :cutoffDate")
    List<User> findRecentUsers(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    List<User> findBySubscriptionType(String subscriptionType);
    
    List<User> findBySubscriptionActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.subscriptionActive = true AND u.subscriptionEndDate < CURRENT_TIMESTAMP")
    List<User> findUsersWithExpiredSubscriptions();
    
    @Query("SELECT u FROM User u WHERE u.subscriptionActive = true AND u.subscriptionEndDate BETWEEN CURRENT_TIMESTAMP AND :expiryDate")
    List<User> findUsersWithSubscriptionsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);
    
    @Query("SELECT u FROM User u WHERE u.subscriptionType IN ('PREMIUM', 'PROFESSIONAL') AND u.subscriptionActive = true")
    List<User> findPremiumUsers();
    
    List<User> findByCreditsRemainingGreaterThan(Integer credits);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR " +
           " LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:enabled IS NULL OR u.enabled = :enabled)")
    Page<User> searchUsers(
        @Param("keyword") String keyword,
        @Param("role") UserRole role,
        @Param("enabled") Boolean enabled,
        Pageable pageable
    );
    
    @Query("SELECT u FROM User u WHERE LOWER(u.birthLocation) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<User> findByBirthLocationContaining(@Param("location") String location);
    
    List<User> findByBirthCountry(String birthCountry);
    
    @Query("SELECT u FROM User u WHERE " +
           "u.birthLatitude BETWEEN :minLat AND :maxLat AND " +
           "u.birthLongitude BETWEEN :minLon AND :maxLon")
    List<User> findUsersInGeographicArea(
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLon") Double minLon,
        @Param("maxLon") Double maxLon
    );
    
    List<User> findByFailedLoginAttemptsGreaterThan(Integer attempts);
    
    @Query("SELECT u FROM User u WHERE u.accountLockedAt IS NOT NULL AND (u.accountUnlockedAt IS NULL OR u.accountLockedAt > u.accountUnlockedAt)")
    List<User> findLockedAccounts();
    
    List<User> findByLastLoginIp(String ip);
    
    List<User> findByPasswordChangedAtAfter(LocalDateTime date);
    
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts > 3 OR u.accountLockedAt IS NOT NULL")
    List<User> findUsersWithSuspiciousActivity();
    
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();
    
    @Query("SELECT u.subscriptionType, COUNT(u) FROM User u GROUP BY u.subscriptionType")
    List<Object[]> countUsersBySubscriptionType();
    
    @Query("SELECT u.sunSign, COUNT(u) FROM User u WHERE u.sunSign IS NOT NULL GROUP BY u.sunSign ORDER BY COUNT(u) DESC")
    List<Object[]> countUsersBySunSign();
    
    @Query("SELECT u.dominantElement, COUNT(u) FROM User u WHERE u.dominantElement IS NOT NULL GROUP BY u.dominantElement")
    List<Object[]> countUsersByDominantElement();
    
    @Query("SELECT YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u) FROM User u " +
           "WHERE u.createdAt >= :startDate " +
           "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
           "ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<Object[]> getUserRegistrationStatistics(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT DATE(u.lastLogin), COUNT(DISTINCT u) FROM User u " +
           "WHERE u.lastLogin >= :startDate " +
           "GROUP BY DATE(u.lastLogin) " +
           "ORDER BY DATE(u.lastLogin)")
    List<Object[]> getDailyActiveUsersCount(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT u FROM User u WHERE u.chartsGenerated > 0 ORDER BY u.chartsGenerated DESC, u.lastLogin DESC")
    List<User> findTopChartGenerators(Pageable pageable);
    
    @Query("SELECT SUM(u.chartsGenerated) FROM User u")
    Long getTotalChartsGenerated();
    
    @Query("SELECT AVG(u.profileCompletionPercentage) FROM User u WHERE u.profileCompletionPercentage > 0")
    Double getAverageProfileCompletion();
    
    @Query("SELECT u FROM User u WHERE " +
           "u.enabled = false AND " +
           "u.createdAt < :cutoffDate AND " +
           "(u.lastLogin IS NULL OR u.lastLogin < :cutoffDate)")
    List<User> findUsersEligibleForDeletion(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT u FROM User u WHERE " +
           "u.emailVerified = false AND " +
           "u.createdAt < :cutoffDate")
    List<User> findUsersWithOldUnverifiedEmails(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT u FROM User u WHERE " +
           "u.natalChart IS NULL AND " +
           "u.birthDateTime IS NOT NULL AND " +
           "u.birthLatitude IS NOT NULL AND " +
           "u.birthLongitude IS NOT NULL")
    List<User> findUsersNeedingChartCalculation();
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :loginTime, u.lastActiveDate = :loginTime, u.lastLoginIp = :ip WHERE u.username = :username")
    void updateLastLogin(@Param("username") String username, @Param("loginTime") LocalDateTime loginTime, @Param("ip") String ip);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.loginStreak = :streak WHERE u.username = :username")
    void updateLoginStreak(@Param("username") String username, @Param("streak") Integer streak);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.chartsGenerated = COALESCE(u.chartsGenerated, 0) + 1 WHERE u.username = :username")
    void incrementChartsGenerated(@Param("username") String username);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.profileCompletionPercentage = :percentage WHERE u.id = :userId")
    void updateProfileCompletion(@Param("userId") Long userId, @Param("percentage") Integer percentage);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.username = :username")
    void clearFailedLoginAttempts(@Param("username") String username);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLockedAt = :lockTime WHERE u.username = :username")
    void lockUserAccount(@Param("username") String username, @Param("lockTime") LocalDateTime lockTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountUnlockedAt = :unlockTime WHERE u.username = :username")
    void unlockUserAccount(@Param("username") String username, @Param("unlockTime") LocalDateTime unlockTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.emailVerified = true, u.emailVerifiedAt = :verificationTime WHERE u.email = :email")
    void markEmailAsVerified(@Param("email") String email, @Param("verificationTime") LocalDateTime verificationTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled = false WHERE u.username = :username")
    void softDeleteUser(@Param("username") String username);
    
    @Query(value = "SELECT * FROM users u WHERE " +
                   "u.birth_latitude IS NOT NULL AND u.birth_longitude IS NOT NULL AND " +
                   "(6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(u.birth_latitude)) * " +
                   "COS(RADIANS(u.birth_longitude) - RADIANS(:lon)) + " +
                   "SIN(RADIANS(:lat)) * SIN(RADIANS(u.birth_latitude)))) <= :distance",
           nativeQuery = true)
    List<User> findUsersWithinDistance(@Param("lat") Double latitude, 
                                       @Param("lon") Double longitude, 
                                       @Param("distance") Double distanceKm);
    
    @Query(value = "SELECT " +
                   "COUNT(*) as total_users, " +
                   "COUNT(CASE WHEN enabled = true THEN 1 END) as active_users, " +
                   "COUNT(CASE WHEN email_verified = true THEN 1 END) as verified_users, " +
                   "COUNT(CASE WHEN birth_date_time IS NOT NULL AND birth_latitude IS NOT NULL AND birth_longitude IS NOT NULL THEN 1 END) as users_with_birth_data, " +
                   "COUNT(CASE WHEN chart_calculated = true THEN 1 END) as users_with_charts, " +
                   "COUNT(CASE WHEN subscription_active = true THEN 1 END) as premium_users, " +
                   "SUM(COALESCE(charts_generated, 0)) as total_charts_generated, " +
                   "AVG(COALESCE(profile_completion_percentage, 0)) as avg_profile_completion " +
                   "FROM users",
           nativeQuery = true)
    Object[] getComprehensiveUserStatistics();
    
    @Query(value = "SELECT email, COUNT(*) as count FROM users " +
                   "GROUP BY LOWER(email) HAVING COUNT(*) > 1",
           nativeQuery = true)
    List<Object[]> findDuplicateEmails();
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET last_user_agent = NULL WHERE last_login < :cutoffDate",
           nativeQuery = true)
    void cleanupOldSessionData(@Param("cutoffDate") LocalDateTime cutoffDate);
}
