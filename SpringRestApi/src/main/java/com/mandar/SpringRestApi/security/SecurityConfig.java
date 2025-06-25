package com.mandar.SpringRestApi.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.mandar.SpringRestApi.config.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final RSAKeyProperties rsaKeys;
        private RSAKey rsaKey;


        public SecurityConfig(RSAKeyProperties rsaKeys) {
                this.rsaKeys = rsaKeys;
        }

        @Bean
        public JWKSource<SecurityContext> jwkSource(){
             rsaKey = Jwks.generateRsa();
             JWKSet jwkSet = new JWKSet(rsaKey);
             return (jwkSelector, context) -> jwkSelector.select(jwkSet);
        }

        @Bean
        public static PasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder();
        }


//        @Bean
//        public InMemoryUserDetailsManager users() {
//                return new InMemoryUserDetailsManager(
//                                User.withUsername("mrd")
//                                                .password("{noop}mrd")
//                                                .authorities("read")
//                                                .build());
//        }

        @Bean
        public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
                var authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                return new ProviderManager(authProvider);
        }

        @Bean
        JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks){
                return new NimbusJwtEncoder(jwks);
        }

        @Bean
        JwtDecoder jwtDecoder() throws JOSEException {
                return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/token", "/", "/swagger-ui/**", "/v3/api-docs/**",
                                                                "/test2")
                                                .permitAll()
                                                .requestMatchers("/test").authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless
                                                                                                        // session
                                )
                                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt); // Enable JWT
                                // TODO: remove these after upgrading the DB from H2 infile to SQL or any other DB
                                http.csrf().disable();
                                http.headers().frameOptions().disable();

                return http.build();

        }

}
