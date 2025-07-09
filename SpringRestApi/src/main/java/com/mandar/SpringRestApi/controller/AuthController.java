package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.model.Account;
import com.mandar.SpringRestApi.payload.auth.*;
import com.mandar.SpringRestApi.service.AccountService;
import com.mandar.SpringRestApi.service.TokenService;
import com.mandar.SpringRestApi.util.constants.AccountError;
import com.mandar.SpringRestApi.util.constants.AccountSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Controller for account management")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    private AccountService accountService;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/token")
    @Operation(summary = "Generate new token")
    @ApiResponse(responseCode = "400", description = "Authentication error")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        log.info("Login attempt for email: {}", userLoginDTO.getEmail());

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + " " + e.getMessage());
            return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/users/add", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and password length between 6 & 20")
    @ApiResponse(responseCode = "201", description = "Account added")
    @ApiResponse(responseCode = "200", description = "Account added")
    @Operation(summary = "Add a new user")
    public ResponseEntity<Map<String, String>> addUser(@Valid @RequestBody AccountDTO accountDTO) {

        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            account.setAuthorities(accountDTO.getAuthorities());
            accountService.save(account);
            Map<String, String> response = new HashMap<>();
            response.put("message", AccountSuccess.ACCOUNT_ADDED.toString());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

//    @GetMapping(value = "/users", produces = "application/json")
//    @Operation(summary = "List all users")
//    public List<Account> listUsers(@Valid @RequestBody AccountDTO accountDTO) {
//        return accountService.findAll();
//    }


    @GetMapping(value = "/users", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Please check access token")
    @Operation(summary = "List all users")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<List<AccountViewDTO>> getAllAccounts() {
        List<AccountViewDTO> accounts = new ArrayList<>();

        for (Account account : accountService.findAll()) {
            accounts.add(new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities()));
        }

        return ResponseEntity.ok(accounts);
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Please check access token")
    @ApiResponse(responseCode = "403", description = "Scope restriction")
    @Operation(summary = "View profile")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            ProfileDTO profileDTO = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities());
            return ResponseEntity.ok(profileDTO);
        }
        return null;

    }

    @GetMapping(value = "/profile", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Please check access token")
    @ApiResponse(responseCode = "403", description = "Scope restriction")
    @Operation(summary = "View profile")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<ProfileDTO> updatePassword(Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            ProfileDTO profileDTO = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities());
            return ResponseEntity.ok(profileDTO);
        }
        return null;

    }


}
