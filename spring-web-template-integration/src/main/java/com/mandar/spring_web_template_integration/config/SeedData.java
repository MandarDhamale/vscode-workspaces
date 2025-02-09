package com.mandar.spring_web_template_integration.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.services.PostService;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        
        Account account01 = new Account();
        Account account02 = new Account();

        account01.setEmail("mrd@gmail.com");
        account01.setPassword("pwd");
        account01.setFirstname("mandar");
        account01.setLastname("dhamale");

        account02.setEmail("mrd2@gmail.com");
        account02.setPassword("pwd2");
        account02.setFirstname("mandar2");
        account02.setLastname("dhamale2");

        accountService.save(account01);
        accountService.save(account02);



        List<Post> posts = postService.getAll();

        if(posts.size() == 0){


            Post post01 = new Post();
            post01.setTitle("Post 01");            
            post01.setBody("Post 01 body ...");
            post01.setAccount(account01);

            Post post02 = new Post();
            post02.setTitle("Post 02");            
            post02.setBody("Post 02 body ...");
            post02.setAccount(account01);
        

            postService.save(post01);
            postService.save(post02);

        }

    }
    


}
