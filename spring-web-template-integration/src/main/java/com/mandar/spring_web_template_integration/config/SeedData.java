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

        Set<Authority> all_authorities = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(all_authorities::add);
        authorityService.findById(Privilege.RESET_ANY_USER_PASSWORD.getId()).ifPresent(all_authorities::add);

        Set<Authority> adminPanelAuthority = new HashSet<>();
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getId()).ifPresent(adminPanelAuthority::add);

        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        account01.setEmail("admin@iprocure.com");
        account01.setPassword("pass787");
        account01.setFirstname("admin");
        account01.setLastname("admin");
        account01.setRole(Roles.ADMIN.getRole());
        account01.setAuthorities(adminPanelAuthority);

        account02.setEmail("editor@iprocure.com");
        account02.setPassword("pass787");
        account02.setFirstname("editor");
        account02.setLastname("editor");
        account02.setRole(Roles.EDITOR.getRole());

        account03.setEmail("user@iprocure.com");
        account03.setPassword("pass787");
        account03.setFirstname("user");
        account03.setLastname("user");

        account04.setEmail("super_editor@iprocure.com");
        account04.setPassword("pass787");
        account04.setFirstname("super_editor");
        account04.setLastname("super_editor");
        account04.setRole(Roles.EDITOR.getRole());
        account04.setAuthorities(all_authorities);

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);

        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {

            Post post01 = new Post();
            post01.setTitle("3M");
            post01.setBody("Supplier of adhesive tapes");
            post01.setAccount(account01);

            Post post02 = new Post();
            post02.setTitle("Dell");
            post02.setBody("Supplier of computers and laptops");
            post02.setAccount(account02);

            Post post03 = new Post();
            post03.setTitle("Acer Corp.");
            post03.setBody("Supplier of computers and laptops");
            post03.setAccount(account03);


            postService.save(post01);
            postService.save(post02);
            postService.save(post03);

        }

    }

}
