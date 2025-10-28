package com.cosmic.astrology.security;

import com.cosmic.astrology.security.JwtAuthenticationEntryPoint;
import com.cosmic.astrology.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Enhanced CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ✅ Allow multiple origins for development
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:8081",
            "http://localhost:3000",
            "http://127.0.0.1:8081"
        ));
        
        // ✅ Allow all HTTP methods including OPTIONS for preflight
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // ✅ Allow all headers
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        
        // ✅ Allow credentials for authentication
        configuration.setAllowCredentials(true);
        
        // ✅ Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // ✅ Enhanced Security Filter Chain
   @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // ✅ CORS configuration first
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        
        // ✅ Disable CSRF for REST API
        .csrf(csrf -> csrf.disable())
        
        // ✅ CRITICAL: Configure authorization BEFORE adding JWT filter
        .authorizeHttpRequests(authz -> authz
            // Allow OPTIONS for CORS preflight
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            
            // ✅ CRITICAL: Allow birth chart endpoints WITHOUT authentication
            .requestMatchers("/api/birth-chart/**").permitAll()
            
            // Allow auth endpoints
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/test/**").permitAll()
            
            // All other requests require authentication
            .anyRequest().authenticated()
        )
        
        // ✅ Configure exception handling
        .exceptionHandling(exception -> 
            exception.authenticationEntryPoint(unauthorizedHandler))
        
        // ✅ Stateless session management
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // ✅ Add JWT filter AFTER authorization configuration
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
}