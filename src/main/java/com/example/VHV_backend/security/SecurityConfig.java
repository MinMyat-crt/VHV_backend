package com.example.VHV_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    private VHVUserDetailsService vhvUserDetailsService;
    @Autowired
    public SecurityConfig(VHVUserDetailsService vhvUserDetailsService,JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.vhvUserDetailsService = vhvUserDetailsService;
        this.jwtAuthEntryPoint=jwtAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF
        http.csrf(csrf -> csrf.disable());

        // Set custom authentication entry point for handling unauthorized access
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(jwtAuthEntryPoint)
        );

        // Set session management to stateless
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Define authorization rules
        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/api/auth/**").permitAll()  // Permit all requests to /api/auth/**
                        .anyRequest().authenticated()                 // Require authentication for other requests
        );

        // Enable HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // Add the JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }
}
