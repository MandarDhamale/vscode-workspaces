package com.mandar.spring_web_template_integration.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.services.AccountService;

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
    public String registerUser(@ModelAttribute Account account) {

        accountService.save(account);
        return "account_views/register:/";

    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "account_views/profile";
    }

}
