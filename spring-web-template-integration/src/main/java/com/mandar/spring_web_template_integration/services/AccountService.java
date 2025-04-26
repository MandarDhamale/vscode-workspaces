package com.mandar.spring_web_template_integration.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Authority;
import com.mandar.spring_web_template_integration.repositories.AccountRepository;
import com.mandar.spring_web_template_integration.util.constants.Roles;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) {

        // Check if the ID is null to avoid unnecessary DB queries
        if (account.getId() != null) {
            Optional<Account> optionalCurrentAccount = accountRepository.findById(account.getId());

            if (optionalCurrentAccount.isPresent()) {
                Account currentAccount = optionalCurrentAccount.get();

                // Debugging logs
                System.out.println("Received password: " + account.getPassword());
                System.out.println("Stored hashed password: " + currentAccount.getPassword());

                // Only hash if the password is in plain text
                if (!account.getPassword().isEmpty() && !account.getPassword().startsWith("$2a$")) {
                    System.out.println("Password changed. Encoding new password.");
                    account.setPassword(passwordEncoder.encode(account.getPassword()));
                } else {
                    System.out.println("Password unchanged. Keeping the old hash.");
                    account.setPassword(currentAccount.getPassword());
                }

            }
        } else {
            // New account, encode password
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        // Ensure default values
        if (account.getRole() == null) {
            account.setRole(Roles.USER.getRole());
        }

        if (account.getPhoto() == null) {
            String path = "/images/default_profile_picture.jpg";
            account.setPhoto(path);
        }

        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);

        if (!optionalAccount.isPresent()) {

            throw new UsernameNotFoundException(email);

        }

        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));
        System.out.println("account role: " + account.getRole());

        for (Authority _auth : account.getAuthorities()) {
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
            System.out.println("auth name: " + _auth.getName());
        }

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);

    }

    public Optional<Account> findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }

    public Optional<Account> findById(Long id) {

        return accountRepository.findById(id);

    }

    public Optional<Account> findByToken(String token) {

        return accountRepository.findByPasswordResetToken(token);

    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


}
