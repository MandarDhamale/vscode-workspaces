package com.mandar.SpringRestApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/")
    public String home(){
        return "Hello World!";
    }

    @GetMapping("/test")
    public String test(){
        return "Test API";
    }

    @GetMapping("/test2")
    public String test2(){
        return "Test2 API";
    }

    
}
