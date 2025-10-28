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

/**
 * Comprehensive User Repository for Vedic Astrology Application
 * Provides advanced query methods for user management, statistics, and business logic
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // ================ BASIC FINDER METHODS ================
    
    /**
     * Find user by username (case-insensitive)
     */
    Optional<User> findByUsername(String username);
    
    
    /**
     * Find user by username ignoring case
     */
    Optional<User> findByUsernameIgnoreCase(String username);
    
    /**
     * Find user by email (case-insensitive)
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by email ignoring case
     */
    Optional<User> findByEmailIgnoreCase(String email);
    
    /**
     * Check if username exists
     */
    Boolean existsByUsername(String username);
    
    /**
     * Check if username exists ignoring case
     */
    Boolean existsByUsernameIgnoreCase(String username);
    
    /**
     * Check if email exists
     */
    Boolean existsByEmail(String email);
    
    /**
     * Check if email exists ignoring case
     */
    Boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Find user by username or email
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);
    
    /**
     * Find user by username or email ignoring case
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:identifier) OR LOWER(u.email) = LOWER(:identifier)")
    Optional<User> findByUsernameOrEmailIgnoreCase(@Param("identifier") String identifier);
    
    // ================ USER STATUS AND ROLE QUERIES ================
    
    /**
     * Find all users by role
     */
    List<User> findByRole(UserRole role);
    
    /**
     * Find users by role with pagination
     */
    Page<User> findByRole(UserRole role, Pageable pageable);
    
    /**
     * Find all enabled users
     */
    List<User> findByEnabledTrue();
    
    /**
     * Find all disabled users
     */
    List<User> findByEnabledFalse();
    
    /**
     * Find enabled users by role
     */
    List<User> findByRoleAndEnabledTrue(UserRole role);
    
    /**
     * Find users with verified email
     */
    List<User> findByEmailVerifiedTrue();
    
    /**
     * Find users with unverified email
     */
    List<User> findByEmailVerifiedFalse();
    
    /**
     * Find users by email verification status and enabled status
     */
    List<User> findByEmailVerifiedAndEnabled(Boolean emailVerified, Boolean enabled);
    
    // ================ BIRTH DATA AND CHART QUERIES ================
    
    /**
     * Find users with complete birth data
     */
    @Query("SELECT u FROM User u WHERE u.birthDateTime IS NOT NULL AND u.birthLatitude IS NOT NULL AND u.birthLongitude IS NOT NULL AND u.birthLocation IS NOT NULL")
    List<User> findUsersWithCompleteBirthData();
    
    /**
     * Find users with complete birth data and pagination
     */
    @Query("SELECT u FROM User u WHERE u.birthDateTime IS NOT NULL AND u.birthLatitude IS NOT NULL AND u.birthLongitude IS NOT NULL AND u.birthLocation IS NOT NULL")
    Page<User> findUsersWithCompleteBirthData(Pageable pageable);
    
    /**
     * Find users without complete birth data
     */
    @Query("SELECT u FROM User u WHERE u.birthDateTime IS NULL OR u.birthLatitude IS NULL OR u.birthLongitude IS NULL OR u.birthLocation IS NULL")
    List<User> findUsersWithIncompleteBirthData();
    
    /**
     * Find users with calculated charts
     */
    List<User> findByChartCalculatedTrue();
    
    /**
     * Find users without calculated charts
     */
    List<User> findByChartCalculatedFalse();
    
    /**
     * Find users with charts calculated after specific date
     */
    List<User> findByChartCalculatedAtAfter(LocalDateTime date);
    
    /**
     * Find users by sun sign
     */
    List<User> findBySunSign(String sunSign);
    
    /**
     * Find users by moon sign
     */
    List<User> findByMoonSign(String moonSign);
    
    /**
     * Find users by rising sign
     */
    List<User> findByRisingSign(String risingSign);
    
    /**
     * Find users by dominant element
     */
    List<User> findByDominantElement(String dominantElement);
    
    /**
     * Find users by multiple astrological criteria
     */
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
    
    // ================ ACTIVITY AND ENGAGEMENT QUERIES ================
    
    /**
     * Find users who logged in after specific date
     */
    List<User> findByLastLoginAfter(LocalDateTime date);
    
    /**
     * Find users who logged in between dates
     */
    List<User> findByLastLoginBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Find users who haven't logged in for specified days
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin < :cutoffDate OR u.lastLogin IS NULL")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Find users with login streak greater than specified number
     */
    List<User> findByLoginStreakGreaterThan(Integer streak);
    
    /**
     * Find users who generated charts after specific date
     */
    List<User> findByChartsGeneratedGreaterThan(Integer count);
    
    /**
     * Find most active users by charts generated
     */
    @Query("SELECT u FROM User u WHERE u.chartsGenerated > 0 ORDER BY u.chartsGenerated DESC")
    List<User> findMostActiveUsers(Pageable pageable);
    
    /**
     * Find users created between dates
     */
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Find users created today
     */
    @Query("SELECT u FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    List<User> findUsersCreatedToday();
    
    /**
     * Find users created in last N days
     */
    @Query("SELECT u FROM User u WHERE u.createdAt >= :cutoffDate")
    List<User> findRecentUsers(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // ================ SUBSCRIPTION AND BILLING QUERIES ================
    
    /**
     * Find users by subscription type
     */
    List<User> findBySubscriptionType(String subscriptionType);
    
    /**
     * Find users with active subscriptions
     */
    List<User> findBySubscriptionActiveTrue();
    
    /**
     * Find users with expired subscriptions
     */
    @Query("SELECT u FROM User u WHERE u.subscriptionActive = true AND u.subscriptionEndDate < CURRENT_TIMESTAMP")
    List<User> findUsersWithExpiredSubscriptions();
    
    /**
     * Find users with subscriptions expiring soon
     */
    @Query("SELECT u FROM User u WHERE u.subscriptionActive = true AND u.subscriptionEndDate BETWEEN CURRENT_TIMESTAMP AND :expiryDate")
    List<User> findUsersWithSubscriptionsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);
    
    /**
     * Find premium users (PREMIUM or PROFESSIONAL)
     */
    @Query("SELECT u FROM User u WHERE u.subscriptionType IN ('PREMIUM', 'PROFESSIONAL') AND u.subscriptionActive = true")
    List<User> findPremiumUsers();
    
    /**
     * Find users with remaining credits
     */
    List<User> findByCreditsRemainingGreaterThan(Integer credits);
    
    // ================ SEARCH AND FILTERING QUERIES ================
    
    /**
     * Search users by name (first name or last name)
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);
    
    /**
     * Search users by multiple criteria
     */
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
    
    /**
     * Find users by birth location
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.birthLocation) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<User> findByBirthLocationContaining(@Param("location") String location);
    
    /**
     * Find users by birth country
     */
    List<User> findByBirthCountry(String birthCountry);
    
    /**
     * Find users in geographic area (bounding box)
     */
    @Query("SELECT u FROM User u WHERE " +
           "u.birthLatitude BETWEEN :minLat AND :maxLat AND " +
           "u.birthLongitude BETWEEN :minLon AND :maxLon")
    List<User> findUsersInGeographicArea(
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLon") Double minLon,
        @Param("maxLon") Double maxLon
    );
    
    // ================ SECURITY AND AUDIT QUERIES ================
    
    /**
     * Find users with failed login attempts
     */
    List<User> findByFailedLoginAttemptsGreaterThan(Integer attempts);
    
    /**
     * Find locked accounts
     */
    @Query("SELECT u FROM User u WHERE u.accountLockedAt IS NOT NULL AND (u.accountUnlockedAt IS NULL OR u.accountLockedAt > u.accountUnlockedAt)")
    List<User> findLockedAccounts();
    
    /**
     * Find users by last login IP
     */
    List<User> findByLastLoginIp(String ip);
    
    /**
     * Find users who changed password recently
     */
    List<User> findByPasswordChangedAtAfter(LocalDateTime date);
    
    /**
     * Find users with suspicious activity (multiple IPs, etc.)
     */
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts > 3 OR u.accountLockedAt IS NOT NULL")
    List<User> findUsersWithSuspiciousActivity();
    
    // ================ STATISTICS AND REPORTING QUERIES ================
    
    /**
     * Count users by role
     */
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();
    
    /**
     * Count users by subscription type
     */
    @Query("SELECT u.subscriptionType, COUNT(u) FROM User u GROUP BY u.subscriptionType")
    List<Object[]> countUsersBySubscriptionType();
    
    /**
     * Count users by sign
     */
    @Query("SELECT u.sunSign, COUNT(u) FROM User u WHERE u.sunSign IS NOT NULL GROUP BY u.sunSign ORDER BY COUNT(u) DESC")
    List<Object[]> countUsersBySunSign();
    
    /**
     * Count users by dominant element
     */
    @Query("SELECT u.dominantElement, COUNT(u) FROM User u WHERE u.dominantElement IS NOT NULL GROUP BY u.dominantElement")
    List<Object[]> countUsersByDominantElement();
    
    /**
     * Get user registration statistics by month
     */
    @Query("SELECT YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u) FROM User u " +
           "WHERE u.createdAt >= :startDate " +
           "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
           "ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<Object[]> getUserRegistrationStatistics(@Param("startDate") LocalDateTime startDate);
    
    /**
     * Get daily active users count
     */
    @Query("SELECT DATE(u.lastLogin), COUNT(DISTINCT u) FROM User u " +
           "WHERE u.lastLogin >= :startDate " +
           "GROUP BY DATE(u.lastLogin) " +
           "ORDER BY DATE(u.lastLogin)")
    List<Object[]> getDailyActiveUsersCount(@Param("startDate") LocalDateTime startDate);
    
    /**
     * Get users with highest chart generation activity
     */
    @Query("SELECT u FROM User u WHERE u.chartsGenerated > 0 ORDER BY u.chartsGenerated DESC, u.lastLogin DESC")
    List<User> findTopChartGenerators(Pageable pageable);
    
    /**
     * Count total charts generated
     */
    @Query("SELECT SUM(u.chartsGenerated) FROM User u")
    Long getTotalChartsGenerated();
    
    /**
     * Get average profile completion percentage
     */
    @Query("SELECT AVG(u.profileCompletionPercentage) FROM User u WHERE u.profileCompletionPercentage > 0")
    Double getAverageProfileCompletion();
    
    // ================ MAINTENANCE AND CLEANUP QUERIES ================
    
    /**
     * Find users eligible for deletion (inactive for long time)
     */
    @Query("SELECT u FROM User u WHERE " +
           "u.enabled = false AND " +
           "u.createdAt < :cutoffDate AND " +
           "(u.lastLogin IS NULL OR u.lastLogin < :cutoffDate)")
    List<User> findUsersEligibleForDeletion(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Find users with old unverified emails
     */
    @Query("SELECT u FROM User u WHERE " +
           "u.emailVerified = false AND " +
           "u.createdAt < :cutoffDate")
    List<User> findUsersWithOldUnverifiedEmails(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Find users with empty natal charts that have complete birth data
     */
    @Query("SELECT u FROM User u WHERE " +
           "u.natalChart IS NULL AND " +
           "u.birthDateTime IS NOT NULL AND " +
           "u.birthLatitude IS NOT NULL AND " +
           "u.birthLongitude IS NOT NULL")
    List<User> findUsersNeedingChartCalculation();
    
    // ================ UPDATE QUERIES ================
    
    /**
     * Update user last login time
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :loginTime, u.lastActiveDate = :loginTime, u.lastLoginIp = :ip WHERE u.username = :username")
    void updateLastLogin(@Param("username") String username, @Param("loginTime") LocalDateTime loginTime, @Param("ip") String ip);
    
    /**
     * Update user login streak
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.loginStreak = :streak WHERE u.username = :username")
    void updateLoginStreak(@Param("username") String username, @Param("streak") Integer streak);
    
    /**
     * Increment charts generated count
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.chartsGenerated = COALESCE(u.chartsGenerated, 0) + 1 WHERE u.username = :username")
    void incrementChartsGenerated(@Param("username") String username);
    
    /**
     * Update profile completion percentage
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.profileCompletionPercentage = :percentage WHERE u.id = :userId")
    void updateProfileCompletion(@Param("userId") Long userId, @Param("percentage") Integer percentage);
    
    /**
     * Clear failed login attempts
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.username = :username")
    void clearFailedLoginAttempts(@Param("username") String username);
    
    /**
     * Lock user account
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLockedAt = :lockTime WHERE u.username = :username")
    void lockUserAccount(@Param("username") String username, @Param("lockTime") LocalDateTime lockTime);
    
    /**
     * Unlock user account
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountUnlockedAt = :unlockTime WHERE u.username = :username")
    void unlockUserAccount(@Param("username") String username, @Param("unlockTime") LocalDateTime unlockTime);
    
    /**
     * Update email verification status
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.emailVerified = true, u.emailVerifiedAt = :verificationTime WHERE u.email = :email")
    void markEmailAsVerified(@Param("email") String email, @Param("verificationTime") LocalDateTime verificationTime);
    
    /**
     * Soft delete user (disable account)
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled = false WHERE u.username = :username")
    void softDeleteUser(@Param("username") String username);
    
    // ================ NATIVE QUERIES FOR COMPLEX OPERATIONS ================
    
    /**
     * Find users within distance from coordinates (using Haversine formula)
     */
    @Query(value = "SELECT * FROM users u WHERE " +
                   "u.birth_latitude IS NOT NULL AND u.birth_longitude IS NOT NULL AND " +
                   "(6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(u.birth_latitude)) * " +
                   "COS(RADIANS(u.birth_longitude) - RADIANS(:lon)) + " +
                   "SIN(RADIANS(:lat)) * SIN(RADIANS(u.birth_latitude)))) <= :distance",
           nativeQuery = true)
    List<User> findUsersWithinDistance(@Param("lat") Double latitude, 
                                       @Param("lon") Double longitude, 
                                       @Param("distance") Double distanceKm);
    
    /**
     * Get comprehensive user statistics
     */
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
    
    /**
     * Find duplicate users by email patterns
     */
    @Query(value = "SELECT email, COUNT(*) as count FROM users " +
                   "GROUP BY LOWER(email) HAVING COUNT(*) > 1",
           nativeQuery = true)
    List<Object[]> findDuplicateEmails();
    
    /**
     * Clean up expired tokens and sessions (if storing in user table)
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET last_user_agent = NULL WHERE last_login < :cutoffDate",
           nativeQuery = true)
    void cleanupOldSessionData(@Param("cutoffDate") LocalDateTime cutoffDate);
}
