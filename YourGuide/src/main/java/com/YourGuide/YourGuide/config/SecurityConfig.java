package com.YourGuide.YourGuide.config;

import com.YourGuide.YourGuide.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .csrf(csrf->csrf.disable())

                .authorizeHttpRequests(auth ->auth.requestMatchers("/api/auth/**")
                .permitAll()
                        .requestMatchers(HttpMethod.GET , "/api/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.GET , "/api/jobs/my-job").hasAuthority("EMPLOYER")
                        .requestMatchers(HttpMethod.POST , "/api/jobs/**").hasAuthority("EMPLOYER")
                        .requestMatchers(HttpMethod.POST,"/api/application").hasAuthority("JOB_SEEKER")
                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
