package com.mandar.spring_web_template_integration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mandar.spring_web_template_integration.util.constants.Privilege;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    private static final String[] WHITELIST = {
            "/", "/login", "/register",
            "/db-console/**", "/css/**", "/fonts/**", "/images/**", "/js/**", "/templates/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authz -> {
                    // Explicitly allow H2 Console
                    authz.requestMatchers(new AntPathRequestMatcher("/db-console/**")).permitAll();
                    authz.requestMatchers(new AntPathRequestMatcher("/profile/**")).authenticated();
                    // authz.requestMatchers(new
                    // AntPathRequestMatcher("/admin/**")).hasRole("ADMIN");
                    authz.requestMatchers(new AntPathRequestMatcher("/editor/**")).hasAnyRole("ADMIN", "EDITOR");
                    authz.requestMatchers(new AntPathRequestMatcher("/admin/**"))
                            .hasAuthority(Privilege.ACCESS_ADMIN_PANEL.getPrivilegeString());

                    // Allow other whitelisted paths
                    for (String pattern : WHITELIST) {
                        authz.requestMatchers(new AntPathRequestMatcher(pattern)).permitAll();
                    }

                    authz.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .usernameParameter("email")
                        .passwordParameter("password"))
                .rememberMe(rememberMe -> rememberMe.key("unique-and-secret")
                .tokenValiditySeconds(14*24*60*60)
                .rememberMeParameter("rememberMe"))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession())
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(false))
                // Disable CSRF **only for H2 Console**
                .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/db-console/**")))

                // Allow H2 Console to be displayed in an iframe
                // .headers(headers -> headers.frameOptions().sameOrigin());
                .headers(headers -> headers.disable());

        return http.build();
    }

}
