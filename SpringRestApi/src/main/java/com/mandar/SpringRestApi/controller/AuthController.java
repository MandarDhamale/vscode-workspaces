package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.payload.auth.Token;
import com.mandar.SpringRestApi.payload.auth.UserLogin;
import com.mandar.SpringRestApi.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager){
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/token")
    public Token token(@RequestBody UserLogin userLogin){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password()));
        return new Token(tokenService.generateToken(authentication));
    }

}
