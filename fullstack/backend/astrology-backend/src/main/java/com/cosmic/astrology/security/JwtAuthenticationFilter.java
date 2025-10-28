package com.cosmic.astrology.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // ✅ Correct SLF4J logger declaration
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // ✅ CRITICAL FIX: Skip JWT authentication for permitted endpoints
        if (requestPath.startsWith("/api/birth-chart/") || 
            requestPath.startsWith("/api/auth/") ||
            requestPath.startsWith("/api/test/") ||
            requestPath.startsWith("/actuator/")) {
            
            // ✅ Correct logger usage - single placeholder
            logger.debug("🔓 Skipping JWT authentication for permitted endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        
        // ✅ Continue with JWT authentication for protected endpoints only
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // ✅ Correct logger usage
                logger.debug("✅ JWT authentication successful for user: {}", username);
            } else {
                logger.debug("⚠️ No valid JWT token found for protected endpoint: {}", requestPath);
            }
        } catch (Exception e) {
            // ✅ Correct error logging with exception - two placeholders plus exception
            logger.error("❌ Cannot set user authentication for path: {} - Error: {}", requestPath, e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    // ✅ parseJwt method (this should already exist in your class)
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
