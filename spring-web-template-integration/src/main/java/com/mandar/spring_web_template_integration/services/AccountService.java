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

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == null) {
            account.setRole(Roles.USER.getRole());
        }

        if (account.getPhoto() == null) {
            String path = "/resources/static/images/default_profile_picture.jpg";
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

}
