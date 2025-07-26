package com.cosmic.astrology.controller;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.service.AstrologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@RestController
@RequestMapping("/api/astrology")
@PreAuthorize("hasRole('CLIENT') or hasRole('ASTROLOGER')")
public class AstrologyController {
    
    @Autowired
    private AstrologyService astrologyService;
    
    /**
     * Get personalized message for the authenticated user
     */
    @GetMapping("/personalized-message")
    public ResponseEntity<?> getPersonalizedMessage(Principal principal) {
        try {
            System.out.println("🎯 Personalized message requested by: " + principal.getName());
            PersonalizedMessageResponse message = astrologyService.getPersonalizedMessage(principal.getName());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            System.err.println("❌ Error fetching personalized message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error fetching personalized message: " + e.getMessage());
        }
    }
    
    /**
     * Get life area influences for the authenticated user
     */
    @GetMapping("/life-area-influences")
    public ResponseEntity<?> getLifeAreaInfluences(Principal principal) {
        try {
            System.out.println("🎯 Life area influences requested by: " + principal.getName());
            List<LifeAreaInfluence> influences = astrologyService.getLifeAreaInfluences(principal.getName());
            return ResponseEntity.ok(influences);
        } catch (Exception e) {
            System.err.println("❌ Error fetching life area influences: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching life area influences: " + e.getMessage());
        }
    }
    
    /**
     * Get user statistics for the authenticated user
     */
    @GetMapping("/user-stats")
    public ResponseEntity<?> getUserStats(Principal principal) {
        try {
            System.out.println("🎯 User stats requested by: " + principal.getName());
            UserStatsResponse stats = astrologyService.getUserStats(principal.getName());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Error fetching user stats: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching user stats: " + e.getMessage());
        }
    }
}
