package com.mandar.spring_web_template_integration.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.util.AppUtil;

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

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@Valid @ModelAttribute Account account, BindingResult bindingResult,
            Principal principal, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "account_views/profile";
        }

        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

        if (optionalAccount.isPresent()) {

            Account accountById = accountService.findById(account.getId()).get();
            accountById.setAge(account.getAge());
            accountById.setDateOfBirth(account.getDateOfBirth());
            accountById.setFirstname(account.getFirstname());
            accountById.setLastname(account.getLastname());
            System.out.println(account.getPassword().toString());
            
            if (!account.getPassword().isEmpty()) {
                accountById.setPassword(account.getPassword());
                System.out.println("Password set again...");
                System.out.println(account.getPassword());
            }

            accountById.setGender(account.getGender());
            accountService.save(accountById);

            redirectAttributes.addFlashAttribute("successMessage", " Profile Updated Successfully");

            return "redirect:/profile";

        } else {
            return "redirect:/login?error";
        }

    }

    @SuppressWarnings("deprecation")
    @PostMapping("/upload_photo")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
            Principal principal) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please upload a file before update");
            return "redirect:/profile";
        } else {
            String fileName = StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), ""));

            try {

                int lenght = 10;
                boolean useLetters = true;
                boolean userNumbers = true;
                String generatedString = RandomStringUtils.random(lenght, useLetters, userNumbers);
                String finalPhotoName = generatedString + fileName;
                String absoluteFileLocation = AppUtil.getUploadPath(finalPhotoName);

                Path path = Paths.get(absoluteFileLocation);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();
                }

                Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                if (optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();
                    Account accountById = accountService.findById(account.getId()).get();
                    String relativeFileLocation = "/uploads/" + finalPhotoName;
                    accountById.setPhoto(relativeFileLocation);
                    accountService.save(accountById);
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }

                redirectAttributes.addFlashAttribute("photoSuccessMessage", " Photo Updated Successfully");

                return "redirect:/profile";

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return "redirect:/profile?error";

    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
    


    
        return "account_views/forgot_password";

    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        // Add logic to handle password reset, such as sending an email.
        
        // For now, let's assume an email is sent successfully.
        redirectAttributes.addFlashAttribute("message", "Password reset link has been sent to your email.");
        
        return "redirect:/login"; // Redirects back to login after success
    }


}
