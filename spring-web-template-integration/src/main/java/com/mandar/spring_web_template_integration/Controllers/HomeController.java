package com.mandar.spring_web_template_integration.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.PostService;

@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @GetMapping("/")
    public String defaultMapping() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model) {

        List<Post> posts = postService.getAll();

        model.addAttribute("posts", posts);

        return "home_views/home";

    }

}
