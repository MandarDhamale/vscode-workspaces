package com.mandar.spring_web_template_integration.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.PostService;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    @Autowired
    PostService postService;
    
    @GetMapping("/")
    public String defaultMapping(){
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model){

        List<Post> posts = postService.getAll();

        model.addAttribute("_posts", posts);

        return "home";


    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/book")
    public String book(){
        return "book";
    }


}
