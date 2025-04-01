package com.mandar.spring_web_template_integration.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String home(Model model,
            @RequestParam(required = false, name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, name = "perPage", defaultValue = "1") String perPage,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page) {

        // List<Post> posts = postService.getAll();
        // model.addAttribute("posts", posts);

        Page<Post> postsOnPage = postService.getAll(Integer.parseInt(page)-1, Integer.parseInt(perPage), sortBy);

        return "home_views/home";
    }

}
