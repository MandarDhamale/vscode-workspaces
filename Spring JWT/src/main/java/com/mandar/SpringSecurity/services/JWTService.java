package com.mandar.SpringSecurity.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

@Service
public class JWTService {

    private final String secretKey;

    public JWTService(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
        System.out.println("JWT Secret Key (Base64): " + secretKey);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 100 * 60 * 60 * 1000;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
