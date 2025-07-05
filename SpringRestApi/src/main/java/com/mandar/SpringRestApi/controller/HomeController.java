package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.model.Account;
import com.mandar.SpringRestApi.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Home page")
    public String home() {
        return "Hello World!";
    }

//    @PostMapping("/account")
//    public ResponseEntity<Account> createAccount(@RequestBody Account account){
//        Account saved = accountService.save(account);
//        return ResponseEntity.ok(saved);
//    }

//    @GetMapping("/accounts")
//    public ResponseEntity<List<Account>> getAllAccounts() {
//        List<Account> accounts = accountService.findAll();
//        return ResponseEntity.ok(accounts);
//    }

}
