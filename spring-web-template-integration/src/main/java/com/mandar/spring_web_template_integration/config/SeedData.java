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



    for(Privilege privilege: Privilege.values()){
        Authority authority = new Authority();
        authority.setId(privilege.getId());
        authority.setName(privilege.getPrivilegeString());
        authorityService.save(authority);
    }


        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();

        Authority adminPannelPrivilege = new Authority();
        adminPannelPrivilege.setId(1l);
        adminPannelPrivilege.setName(Privilege.RESET_ANY_USER_PASSWORD.getPrivilegeString());

        Set<Authority> authorities = new HashSet<>();
        authorities.add(adminPannelPrivilege);


        account01.setEmail("m@ip.com");
        account01.setPassword("mrd");
        account01.setFirstname("mandar");
        account01.setLastname("dhamale");
        account01.setRole(Roles.ADMIN.getRole());
        account01.setAuthorities(authorities);

        account02.setEmail("root@ip.com");
        account02.setPassword("root");
        account02.setFirstname("root");
        account02.setLastname("root");
        account02.setRole(Roles.EDITOR.getRole());

        account03.setEmail("user@ip.com");
        account03.setPassword("user");
        account03.setFirstname("user");
        account03.setLastname("user");

        accountService.save(account01);
        accountService.save(account02);

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
