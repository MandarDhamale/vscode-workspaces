package com.mandar.SpringRestApi.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="rsa")
public record RSAKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    
}
