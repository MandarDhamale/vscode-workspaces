package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.model.Account;
import com.mandar.SpringRestApi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Home Controller", description = "Controller for home management")
public class HomeController {

    @Autowired
    private AccountService accountService;

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

    @PostMapping("/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        Account saved = accountService.save(account);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accounts);
    }
    
}
