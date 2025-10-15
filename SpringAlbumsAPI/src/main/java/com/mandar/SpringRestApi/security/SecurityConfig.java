package com.mandar.SpringRestApi.security;

import com.mandar.SpringRestApi.config.RSAKeyProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RSAKeyProperties rsaKeys;
    private RSAKey rsaKey;

    public SecurityConfig(RSAKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/", "/api/v1/auth/token", "/api/v1/auth/users/add",
                                "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // admin-level endpoints
                        .requestMatchers("/api/v1/auth/users").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_READ")
                        .requestMatchers("/api/v1/auth/users/*/update-authorities").hasAuthority("SCOPE_ADMIN")

                        // album management
                        .requestMatchers(
                                "/api/v1/album/add",
                                "/api/v1/album/albums",
                                "/api/v1/album/albums/*",
                                "/api/v1/album/albums/*/update",
                                "/api/v1/album/*/upload-photos",
                                "/api/v1/album/albums/*/photos/*/update",
                                "/api/v1/album/albums/*/photos/*/delete",
                                "/api/v1/album/albums/*/delete"
                        ).authenticated()

                        //secure photo downloads properly using wildcards
                        .requestMatchers(
                                "/api/v1/album/albums/*/photos/*/download-photo",
                                "/api/v1/album/albums/*/photos/*/download-thumbnail"
                        ).authenticated()

                        // profile endpoints
                        .requestMatchers(
                                "/api/v1/auth/profile",
                                "/api/v1/auth/profile/update-password",
                                "/api/v1/auth/profile/delete"
                        ).authenticated()

                        // fallback
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }
}
