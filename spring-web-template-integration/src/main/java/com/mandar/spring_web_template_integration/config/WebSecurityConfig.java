package com.mandar.spring_web_template_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    private static final String[] WHITELIST = {
        "/",
        "/login",
        "/home",
        "/register",
        "db-console/**",
        "/css/**",
        "/fonts/**",
        "/images/**",
        "/js/**",
        "/templates/**"

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
        .authorizeHttpRequests((authz) -> {

            // // Allow access to static resources
            // authz.requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll();
            // authz.requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll();
            // authz.requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll();
            // authz.requestMatchers(new AntPathRequestMatcher("/fonts/**")).permitAll(); // Add this line

            for (String pattern : WHITELIST) {
                authz.requestMatchers(new AntPathRequestMatcher(pattern)).permitAll();
            }

            authz
                .anyRequest().authenticated();
        });

        //TODO: Remove these after upgrading the DB from H2 infile DB
        http.csrf().disable();
        http.headers().frameOptions().disable();


            return http.build();

    }

    
}
