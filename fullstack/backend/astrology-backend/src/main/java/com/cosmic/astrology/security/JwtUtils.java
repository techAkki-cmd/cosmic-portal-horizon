package com.cosmic.astrology.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${app.jwtSecret:cosmicAstrologySecretKeyForJwtTokenGenerationAndValidation2024SuperSecureKeyThatIsLongEnough}")
    private String jwtSecret;
    
    @Value("${app.jwtExpirationMs:86400000}")
    private int jwtExpirationMs;
    
    /**
     * Generate signing key with proper validation
     */
    private SecretKey getSigningKey() {
        try {
            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                logger.error("❌ JWT secret is null or empty");
                throw new IllegalStateException("JWT secret cannot be null or empty");
            }
            
            // Ensure secret is at least 32 characters (256 bits) for HS256
            if (jwtSecret.length() < 32) {
                logger.error("❌ JWT secret is too short: {} characters. Minimum required: 32", jwtSecret.length());
                throw new IllegalStateException("JWT secret must be at least 32 characters for HS256");
            }
            
            logger.debug("✅ JWT secret validated successfully (length: {})", jwtSecret.length());
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
        } catch (Exception e) {
            logger.error("❌ Error creating signing key: {}", e.getMessage());
            throw new RuntimeException("Failed to create JWT signing key", e);
        }
    }
    
    /**
     * Generate JWT token with comprehensive error handling
     */
    public String generateJwtToken(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                logger.error("❌ Username is null or empty for JWT generation");
                throw new IllegalArgumentException("Username cannot be null or empty");
            }

            logger.debug("🔐 Generating JWT token for user: {}", username);
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
            
            logger.debug("Token expiry time: {}", expiryDate);
            
            // Get signing key
            SecretKey signingKey = getSigningKey();
            
            // Generate token using JJWT 0.12.x API
            String token = Jwts.builder()
                    .subject(username.trim())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .issuer("CosmicAstrology")
                    .signWith(signingKey, Jwts.SIG.HS256)
                    .compact();
                    
            logger.debug("✅ JWT token generated successfully for user: {}", username);
            return token;
                    
        } catch (Exception e) {
            logger.error("❌ Error generating JWT token for user: {} - {}", username, e.getMessage());
            logger.error("Full stack trace:", e);
            throw new RuntimeException("Could not generate JWT token", e);
        }
    }
    
    /**
     * Validate JWT token with detailed error logging
     */
    public boolean validateJwtToken(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            logger.warn("❌ JWT token is null or empty");
            return false;
        }
        
        try {
            SecretKey signingKey = getSigningKey();
            
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(authToken.trim());
                
            logger.debug("✅ JWT token validation successful");
            return true;
            
        } catch (ExpiredJwtException e) {
            logger.warn("❌ JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("❌ JWT token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("❌ Invalid JWT token format: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("❌ JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ JWT validation error: {}", e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Extract username from JWT token
     */
    public String getUserNameFromJwtToken(String token) {
        try {
            SecretKey signingKey = getSigningKey();
            
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token.trim())
                    .getPayload();
                    
            return claims.getSubject();
            
        } catch (Exception e) {
            logger.error("❌ Error extracting username from JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to extract username from token", e);
        }
    }
}
