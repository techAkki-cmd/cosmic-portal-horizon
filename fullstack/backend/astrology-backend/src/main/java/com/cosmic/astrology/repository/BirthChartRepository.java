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
    
    Optional<BirthChart> findByUserAndIsActiveTrue(User user);
    
    List<BirthChart> findByUserOrderByCreatedAtDesc(User user);
    
    Optional<BirthChart> findByUserIdAndIsActiveTrue(Long userId);
    
    List<BirthChart> findByUserAndIsActiveTrueOrderByCreatedAtDesc(User user);
    
    boolean existsByUser(User user);
    
    long countByUser(User user);
    
    List<BirthChart> findByBirthLocationContainingIgnoreCase(String location);
    
    @Query("SELECT bc FROM BirthChart bc WHERE bc.user = :user AND bc.birthDateTime BETWEEN :startDate AND :endDate")
    List<BirthChart> findByUserAndBirthDateTimeBetween(@Param("user") User user, 
                                                      @Param("startDate") java.time.LocalDateTime startDate,
                                                      @Param("endDate") java.time.LocalDateTime endDate);
    
    List<BirthChart> findByTimezone(String timezone);
    
    void deleteByUserAndIsActiveFalse(User user);
}
