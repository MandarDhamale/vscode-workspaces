package com.mandar.SpringRestApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class AccountController {

    @GetMapping("/")
    public String home(){
        return "Hello World!";
    }

    @GetMapping("/test")
    @Tag(name="Test", description="Test API")
    @SecurityRequirement(name="mrd-api")
    public String test(){
        return "Test API";
    }

    @GetMapping("/test2")
    public String test2(){
        return "Test2 API";
    }

    
}
