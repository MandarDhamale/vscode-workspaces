package com.mandar.spring_web_template_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        "/db-console/**",
        "/css/**",
        "/fonts/**",
        "/images/**",
        "/js/**",
        "/templates/**"
    };


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((authz) -> {

                for (String pattern : WHITELIST) {
                    authz.requestMatchers(new AntPathRequestMatcher(pattern)).permitAll();
                }

                authz
                    .anyRequest().authenticated();
            })
            .formLogin(form -> form
                .loginPage("/login") // Specify your login page URL
                .permitAll() // Allow access to the login page for everyone
                .defaultSuccessUrl("/home") // Redirect to /home after successful login
                .failureUrl("/login?error") // Redirect to login with error parameter if login fails.
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL to trigger logout
                .logoutSuccessUrl("/login?logout") // Redirect to /login after logout.  Include a logout parameter if you want.
                .permitAll() // Allow everyone to access the logout URL
                .invalidateHttpSession(true) // Invalidate the HTTP session
                .deleteCookies("JSESSIONID") // Optionally delete the session cookie
            );

        //TODO: Remove these after upgrading the DB from H2 infile DB
        http.csrf().disable();
        http.headers().frameOptions().disable();


        return http.build();

    }


}