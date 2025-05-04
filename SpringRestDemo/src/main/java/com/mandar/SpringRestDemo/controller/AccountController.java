package com.mandar.SpringRestDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    @GetMapping("")
    public String home(){
        return "Hello World!";
    }

    @GetMapping("/")
    public String demo(){
        return "Hello World!";
    }

    @GetMapping("/test")
    public String test(){
        return "Test API";
    }


}
