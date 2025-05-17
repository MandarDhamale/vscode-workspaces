package com.mandar.SpringSecurity.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    @GetMapping("")
    public String home(HttpServletRequest request){
        String home = "Welcome, your session Id is " + request.getSession().getId();
        return home;
    }

    @GetMapping("/csrf_token")
    public String getToken(CsrfToken csrfToken){
        return csrfToken.getToken();
    }

}
