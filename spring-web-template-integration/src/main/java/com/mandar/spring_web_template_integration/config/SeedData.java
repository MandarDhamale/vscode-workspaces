package com.mandar.spring_web_template_integration.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Authority;
import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.services.AuthorityService;
import com.mandar.spring_web_template_integration.services.PostService;
import com.mandar.spring_web_template_integration.util.constants.Privilege;
import com.mandar.spring_web_template_integration.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        // takes the values from enum Privileges and puts them into Authority model
        // which is then stored in db
        for (Privilege privilege : Privilege.values()) {
            Authority authority = new Authority();
            authority.setId(privilege.getId());
            authority.setName(privilege.getPrivilegeString());
            authorityService.save(authority);
        }

        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        authorityService.findById(Privilege.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);

        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();

        account01.setEmail("admin@iprocure.com");
        account01.setPassword("admin");
        account01.setFirstname("admin");
        account01.setLastname("admin");
        account01.setRole(Roles.ADMIN.getRole());
        account01.setAuthorities(authorities);

        account02.setEmail("editor@iprocure.com");
        account02.setPassword("editor");
        account02.setFirstname("editor");
        account02.setLastname("editor");
        account02.setRole(Roles.EDITOR.getRole());
        account02.setAuthorities(authorities);

        account03.setEmail("user@iprocure.com");
        account03.setPassword("user");
        account03.setFirstname("user");
        account03.setLastname("user");

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);

        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {

            Post post01 = new Post();
            post01.setTitle("3M ");
            post01.setBody("Supplier of adhesive tapes");
            post01.setAccount(account01);

            Post post02 = new Post();
            post02.setTitle("Dell");
            post02.setBody("Supplier of computers and laptops");
            post02.setAccount(account01);

            postService.save(post01);
            postService.save(post02);

        }

    }

}
