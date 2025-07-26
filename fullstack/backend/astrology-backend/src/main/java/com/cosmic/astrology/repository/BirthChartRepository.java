package com.cosmic.astrology.repository;

import com.cosmic.astrology.entity.BirthChart;
import com.cosmic.astrology.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BirthChartRepository extends JpaRepository<BirthChart, Long> {
    
    // Find active birth chart for a user
    Optional<BirthChart> findByUserAndIsActiveTrue(User user);
    
    // Find all birth charts for a user
    List<BirthChart> findByUserOrderByCreatedAtDesc(User user);
    
    // Find by user ID
    Optional<BirthChart> findByUserIdAndIsActiveTrue(Long userId);
    
    // Find all active birth charts for a user
    List<BirthChart> findByUserAndIsActiveTrueOrderByCreatedAtDesc(User user);
    
    // Check if user has any birth charts
    boolean existsByUser(User user);
    
    // Count birth charts for a user
    long countByUser(User user);
    
    // Find by birth location (for location-based queries)
    List<BirthChart> findByBirthLocationContainingIgnoreCase(String location);
    
    // Custom query to find birth charts within a date range
    @Query("SELECT bc FROM BirthChart bc WHERE bc.user = :user AND bc.birthDateTime BETWEEN :startDate AND :endDate")
    List<BirthChart> findByUserAndBirthDateTimeBetween(@Param("user") User user, 
                                                      @Param("startDate") java.time.LocalDateTime startDate,
                                                      @Param("endDate") java.time.LocalDateTime endDate);
    
    // Find birth charts by timezone
    List<BirthChart> findByTimezone(String timezone);
    
    // Delete old inactive charts (cleanup method)
    void deleteByUserAndIsActiveFalse(User user);
}
