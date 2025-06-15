package com.mandar.SpringRestApi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    public InMemoryUserDetailsManager users(){
        return new InMemoryUserDetailsManager(
            User.withUsername("mrd")
            .password("{noop}mrd")
            .authorities("read")
            .build()
        );
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{


        http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/token", "/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
        .requestMatchers("/test").authenticated()
        
        );

        return http.build();


        
    }

}
