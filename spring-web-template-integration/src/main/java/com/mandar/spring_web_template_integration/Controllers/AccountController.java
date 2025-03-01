package com.mandar.spring_web_template_integration.Controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.services.AccountService;

import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/register")
    public String register(Model model) {

        Account account = new Account();

        model.addAttribute("account", account);

        return "account_views/register";

    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute Account account, BindingResult result) {

        if (result.hasErrors()) {
            return "account_views/register";
        }

        accountService.save(account);
        return "account_views/register:/";

    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
    
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }
    
        String authUser = principal.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
    
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
        } else {
            model.addAttribute("error", "Account not found.");
            return "error_page"; // Redirect to a custom error page
        }
    
        return "account_views/profile";
    }
    

}
