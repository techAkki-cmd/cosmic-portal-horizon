package com.cosmic.astrology.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Comprehensive JWT Utility Service for Vedic Astrology Application
 * Handles JWT token generation, validation, parsing, and security features
 */
@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    // Configuration properties
    @Value("${app.jwtSecret:cosmicAstrologySecretKeyForJwtTokenGenerationAndValidation2024SuperSecureKeyThatIsLongEnoughForHS256Algorithm}")
    private String jwtSecret;
    
    @Value("${app.jwtExpirationMs:86400000}") // 24 hours default
    private int jwtExpirationMs;
    
    @Value("${app.jwtRefreshExpirationMs:604800000}") // 7 days default  
    private int jwtRefreshExpirationMs;
    
    @Value("${app.jwt.issuer:CosmicAstrology}")
    private String jwtIssuer;
    
    @Value("${app.jwt.audience:cosmic-users}")
    private String jwtAudience;
    
    // Security configurations
    @Value("${app.security.enableTokenRotation:true}")
    private boolean enableTokenRotation;
    
    @Value("${app.security.maxTokensPerUser:5}")
    private int maxTokensPerUser;
    
    // Token blacklist (in production, use Redis or database)
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();
    private final Map<String, Set<String>> userActiveTokens = new ConcurrentHashMap<>();
    
    // Constants
    private static final String TOKEN_TYPE = "JWT";
    private static final String SIGNING_ALGORITHM = "HS256";
    private static final int MIN_SECRET_LENGTH = 32; // 256 bits
    
    // Custom claims
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    private static final String CLAIM_CLIENT_IP = "clientIp";
    private static final String CLAIM_USER_AGENT = "userAgent";
    private static final String CLAIM_SESSION_ID = "sessionId";
    
    /**
     * Generate signing key with comprehensive validation
     */
    private SecretKey getSigningKey() {
        try {
            validateJwtSecret();
            logger.debug("✅ JWT secret validated successfully (length: {})", jwtSecret.length());
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
        } catch (Exception e) {
            logger.error("❌ Error creating signing key: {}", e.getMessage());
            throw new RuntimeException("Failed to create JWT signing key", e);
        }
    }
    
    /**
     * Validate JWT secret configuration
     */
    private void validateJwtSecret() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            logger.error("❌ JWT secret is null or empty");
            throw new IllegalStateException("JWT secret cannot be null or empty");
        }
        
        if (jwtSecret.length() < MIN_SECRET_LENGTH) {
            logger.error("❌ JWT secret is too short: {} characters. Minimum required: {}", 
                        jwtSecret.length(), MIN_SECRET_LENGTH);
            throw new IllegalStateException("JWT secret must be at least " + MIN_SECRET_LENGTH + 
                                          " characters for " + SIGNING_ALGORITHM);
        }
    }
    
    /**
     * Generate JWT token with basic claims
     */
    public String generateJwtToken(String username) {
        return generateJwtToken(username, null, null, null, null, null);
    }
    
    /**
     * Generate JWT token with enhanced claims for AuthService compatibility
     */
    public String generateJwtToken(String username, String role, String email, 
                                  String clientIp, String userAgent) {
        return generateJwtToken(username, role, email, null, clientIp, userAgent);
    }
    
    /**
     * Generate comprehensive JWT token with all claims
     */
    public String generateJwtToken(String username, String role, String email, Long userId,
                                  String clientIp, String userAgent) {
        try {
            validateUsername(username);
            
            logger.debug("🔐 Generating JWT token for user: {} with role: {}", username, role);
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
            String sessionId = UUID.randomUUID().toString();
            
            // Get signing key
            SecretKey signingKey = getSigningKey();
            
            // Build JWT with comprehensive claims
            JwtBuilder jwtBuilder = Jwts.builder()
                    .subject(username.trim())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .notBefore(now)
                    .id(sessionId)
                    .issuer(jwtIssuer)
                    .audience().add(jwtAudience).and()
                    .header().type(TOKEN_TYPE).and()
                    .claim(CLAIM_TOKEN_TYPE, "access")
                    .claim(CLAIM_SESSION_ID, sessionId);
            
            // Add optional claims
            if (userId != null) {
                jwtBuilder.claim(CLAIM_USER_ID, userId);
            }
            if (role != null && !role.trim().isEmpty()) {
                jwtBuilder.claim(CLAIM_ROLE, role.trim());
            }
            if (email != null && !email.trim().isEmpty()) {
                jwtBuilder.claim(CLAIM_EMAIL, email.trim());
            }
            if (clientIp != null && !clientIp.trim().isEmpty()) {
                jwtBuilder.claim(CLAIM_CLIENT_IP, clientIp.trim());
            }
            if (userAgent != null && !userAgent.trim().isEmpty()) {
                jwtBuilder.claim(CLAIM_USER_AGENT, userAgent.trim().substring(0, Math.min(userAgent.length(), 200)));
            }
            
            String token = jwtBuilder.signWith(signingKey, Jwts.SIG.HS256).compact();
            
            // Track active tokens for user
            if (enableTokenRotation) {
                trackUserToken(username, token);
            }
            
            logger.debug("✅ JWT token generated successfully for user: {} (expires: {})", username, expiryDate);
            return token;
                    
        } catch (IllegalArgumentException e) {
            logger.error("❌ Invalid input for JWT generation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("❌ Error generating JWT token for user: {} - {}", username, e.getMessage());
            logger.error("Full stack trace:", e);
            throw new RuntimeException("Could not generate JWT token", e);
        }
    }
    
    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String username, String role, String email, Long userId) {
        try {
            validateUsername(username);
            
            logger.debug("🔄 Generating refresh token for user: {}", username);
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationMs);
            String sessionId = UUID.randomUUID().toString();
            
            SecretKey signingKey = getSigningKey();
            
            String refreshToken = Jwts.builder()
                    .subject(username.trim())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .notBefore(now)
                    .id(sessionId)
                    .issuer(jwtIssuer)
                    .audience().add(jwtAudience).and()
                    .claim(CLAIM_TOKEN_TYPE, "refresh")
                    .claim(CLAIM_USER_ID, userId)
                    .claim(CLAIM_ROLE, role)
                    .claim(CLAIM_EMAIL, email)
                    .signWith(signingKey, Jwts.SIG.HS256)
                    .compact();
            
            logger.debug("✅ Refresh token generated successfully for user: {}", username);
            return refreshToken;
                    
        } catch (Exception e) {
            logger.error("❌ Error generating refresh token for user: {} - {}", username, e.getMessage());
            throw new RuntimeException("Could not generate refresh token", e);
        }
    }
    
    /**
     * Validate JWT token with comprehensive error handling
     */
    public boolean validateJwtToken(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            logger.warn("❌ JWT token is null or empty");
            return false;
        }
        
        String trimmedToken = authToken.trim();
        
        // Check if token is blacklisted
        if (isTokenBlacklisted(trimmedToken)) {
            logger.warn("❌ JWT token is blacklisted");
            return false;
        }
        
        try {
            SecretKey signingKey = getSigningKey();
            
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(signingKey)
                    .requireIssuer(jwtIssuer)
                    .requireAudience(jwtAudience)
                    .build()
                    .parseSignedClaims(trimmedToken);
            
            // Additional validation
            Claims claims = claimsJws.getPayload();
            if (!isTokenValid(claims)) {
                logger.warn("❌ JWT token failed additional validation checks");
                return false;
            }
                
            logger.debug("✅ JWT token validation successful for user: {}", claims.getSubject());
            return true;
            
        } catch (ExpiredJwtException e) {
            logger.warn("❌ JWT token is expired: {} (expired: {})", e.getMessage(), e.getClaims().getExpiration());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("❌ JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.error("❌ Invalid JWT token format: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            logger.error("❌ JWT signature validation failed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("❌ JWT claims string is empty or invalid: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("❌ Unexpected JWT validation error: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract username from JWT token
     */
    public String getUserNameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * Extract expiration date from JWT token (required by AuthService)
     */
    public Date getExpirationDateFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * Extract issued date from JWT token
     */
    public Date getIssuedAtDateFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }
    
    /**
     * Extract role from JWT token
     */
    public String getRoleFromJwtToken(String token) {
        return getClaimFromToken(token, claims -> claims.get(CLAIM_ROLE, String.class));
    }
    
    /**
     * Extract email from JWT token
     */
    public String getEmailFromJwtToken(String token) {
        return getClaimFromToken(token, claims -> claims.get(CLAIM_EMAIL, String.class));
    }
    
    /**
     * Extract user ID from JWT token
     */
    public Long getUserIdFromJwtToken(String token) {
        return getClaimFromToken(token, claims -> claims.get(CLAIM_USER_ID, Long.class));
    }
    
    /**
     * Extract session ID from JWT token
     */
    public String getSessionIdFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }
    
    /**
     * Extract client IP from JWT token
     */
    public String getClientIpFromJwtToken(String token) {
        return getClaimFromToken(token, claims -> claims.get(CLAIM_CLIENT_IP, String.class));
    }
    
    /**
     * Generic method to extract claims from JWT token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.error("❌ Error extracting claim from JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to extract claim from token", e);
        }
    }
    
    /**
     * Extract all claims from JWT token
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            SecretKey signingKey = getSigningKey();
            
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token.trim())
                    .getPayload();
                    
        } catch (Exception e) {
            logger.error("❌ Error extracting claims from JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to extract claims from token", e);
        }
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromJwtToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.error("❌ Error checking token expiration: {}", e.getMessage());
            return true; // Assume expired if we can't check
        }
    }
    
    /**
     * Get remaining time until token expires (in minutes)
     */
    public long getMinutesUntilExpiry(String token) {
        try {
            Date expiration = getExpirationDateFromJwtToken(token);
            long diffInMillies = expiration.getTime() - new Date().getTime();
            return Math.max(0, diffInMillies / (60 * 1000));
        } catch (Exception e) {
            logger.error("❌ Error calculating time until expiry: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Check if token needs refresh (expires within threshold)
     */
    public boolean shouldRefreshToken(String token, long thresholdMinutes) {
        return getMinutesUntilExpiry(token) <= thresholdMinutes;
    }
    
    /**
     * Invalidate/blacklist a token
     */
    public void invalidateToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            tokenBlacklist.add(token.trim());
            
            // Remove from user's active tokens
            try {
                String username = getUserNameFromJwtToken(token);
                removeUserToken(username, token);
                logger.debug("✅ Token invalidated for user: {}", username);
            } catch (Exception e) {
                logger.warn("⚠️ Could not determine username for token invalidation: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Invalidate all tokens for a user
     */
    public void invalidateAllUserTokens(String username) {
        Set<String> userTokens = userActiveTokens.get(username);
        if (userTokens != null) {
            tokenBlacklist.addAll(userTokens);
            userActiveTokens.remove(username);
            logger.debug("✅ All tokens invalidated for user: {}", username);
        }
    }
    
    /**
     * Get token information for debugging/monitoring
     */
    public Map<String, Object> getTokenInfo(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Map<String, Object> info = new HashMap<>();
            
            info.put("subject", claims.getSubject());
            info.put("issuer", claims.getIssuer());
            info.put("audience", claims.getAudience());
            info.put("issuedAt", claims.getIssuedAt());
            info.put("expiration", claims.getExpiration());
            info.put("sessionId", claims.getId());
            info.put("role", claims.get(CLAIM_ROLE));
            info.put("email", claims.get(CLAIM_EMAIL));
            info.put("tokenType", claims.get(CLAIM_TOKEN_TYPE));
            info.put("isExpired", isTokenExpired(token));
            info.put("minutesUntilExpiry", getMinutesUntilExpiry(token));
            info.put("isBlacklisted", isTokenBlacklisted(token));
            
            return info;
        } catch (Exception e) {
            logger.error("❌ Error getting token info: {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }
    
    /**
     * Get JWT configuration information
     */
    public Map<String, Object> getJwtConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("algorithm", SIGNING_ALGORITHM);
        config.put("issuer", jwtIssuer);
        config.put("audience", jwtAudience);
        config.put("expirationMs", jwtExpirationMs);
        config.put("refreshExpirationMs", jwtRefreshExpirationMs);
        config.put("secretLength", jwtSecret.length());
        config.put("tokenRotationEnabled", enableTokenRotation);
        config.put("maxTokensPerUser", maxTokensPerUser);
        config.put("blacklistedTokensCount", tokenBlacklist.size());
        config.put("activeUsersCount", userActiveTokens.size());
        return config;
    }
    
    // ================ PRIVATE HELPER METHODS ================
    
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
    }
    
    private boolean isTokenValid(Claims claims) {
        // Additional custom validation logic
        if (claims.getSubject() == null || claims.getSubject().trim().isEmpty()) {
            return false;
        }
        
        // Check token type if present
        String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
        if (tokenType != null && !"access".equals(tokenType) && !"refresh".equals(tokenType)) {
            return false;
        }
        
        return true;
    }
    
    private boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token.trim());
    }
    
    private void trackUserToken(String username, String token) {
        userActiveTokens.computeIfAbsent(username, k -> ConcurrentHashMap.newKeySet()).add(token);
        
        // Limit number of active tokens per user
        Set<String> userTokens = userActiveTokens.get(username);
        if (userTokens.size() > maxTokensPerUser) {
            // Remove oldest tokens (simplified - in production, track timestamps)
            Iterator<String> iterator = userTokens.iterator();
            while (userTokens.size() > maxTokensPerUser && iterator.hasNext()) {
                String oldToken = iterator.next();
                iterator.remove();
                tokenBlacklist.add(oldToken);
            }
        }
    }
    
    private void removeUserToken(String username, String token) {
        Set<String> userTokens = userActiveTokens.get(username);
        if (userTokens != null) {
            userTokens.remove(token);
            if (userTokens.isEmpty()) {
                userActiveTokens.remove(username);
            }
        }
    }
    
    /**
     * Periodic cleanup of expired tokens from blacklist
     */
    public void cleanupExpiredTokens() {
        try {
            int initialSize = tokenBlacklist.size();
            tokenBlacklist.removeIf(token -> {
                try {
                    return isTokenExpired(token);
                } catch (Exception e) {
                    // If we can't parse the token, remove it
                    return true;
                }
            });
            
            int removedCount = initialSize - tokenBlacklist.size();
            if (removedCount > 0) {
                logger.debug("🧹 Cleaned up {} expired tokens from blacklist", removedCount);
            }
        } catch (Exception e) {
            logger.error("❌ Error during token cleanup: {}", e.getMessage());
        }
    }
}
